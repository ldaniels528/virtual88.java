package org.ldaniels528.javapc.ibmpc.devices.bios.services;

import org.apache.log4j.Logger;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static java.lang.String.format;

/**
 * BIOS Keyboard Software Services Processor
 *
 * @author lawrence.daniels@gmail.com
 */
public class KeyboardSoftwareServices implements InterruptHandler {
    private static final KeyboardSoftwareServices instance = new KeyboardSoftwareServices();
    private final Logger logger = Logger.getLogger(getClass());

    /**
     * Private constructor
     */
    private KeyboardSoftwareServices() {
        super();
    }

    /**
     * Returns the singleton instance of this class
     *
     * @return the singleton instance of this class
     */
    public static KeyboardSoftwareServices getInstance() {
        return instance;
    }

    /**
     * Process the Disk Services Interrupt (INT 13h)
     *
     * @throws X86AssemblyException
     */
    public void process(final IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        try {
            // get the keyboard instance
            final IbmPcKeyboard keyboard = system.getKeyboard();

            // determine what to do
            switch (cpu.AH.get()) {
                case 0x00:
                    waitForKeyPress(cpu, keyboard);
                    break;
                case 0x01:
                    getKeyboardStatus(cpu, keyboard);
                    break;
                case 0x02:
                    readKeyboardFlags(cpu, keyboard);
                    break;
                case 0x03:
                    setTypematicRate(cpu);
                    break;
                case 0x04:
                    setKeyboardClickAdjustment(cpu);
                    break;
                default:
                    throw new X86AssemblyException(String.format("Invalid call (AH = %02X)", cpu.AH.get()));
            }
        } catch (InterruptedException e) {
            throw new X86AssemblyException(e);
        }
    }

    /**
     * <pre>
     * INT 16,0 - Wait for Keypress and Read Character
     * AH = 00
     * on return:
     * 	AH = keyboard scan code
     * 	AL = ASCII character or zero if special function key
     * 	- halts program until key with a scancode is pressed
     * 	- see ~SCAN CODES~
     * </pre>
     *
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param keyboard the given {@link IbmPcKeyboard keyboard} instance
     */
    private void waitForKeyPress(final I8086 cpu, final IbmPcKeyboard keyboard) throws InterruptedException {
        // block read a character
        final String s = keyboard.next(1);
        if (s.length() != 0) {
            final char ch = s.charAt(0);
            final int keyFlags = keyboard.getKeyFlags();
            logger.info(format("Get '%c' key [flags = %02X]", ch, keyFlags));
            cpu.AH.set(0);
            cpu.AL.set(ch);
        }
    }

    /**
     * <pre>
     * INT 16,1 - Get Keyboard Status
     * AH = 01
     * on return:
     * 	ZF = 0 if a key pressed (even Ctrl-Break)
     * 	AX = 0 if no scan code is available
     * 	AH = ~scan code~
     * 	AL = ASCII character or zero if special function key
     * - data code is not removed from buffer
     * - ~Ctrl-Break~ places a zero word in the keyboard buffer but does
     * register a keypress.
     * </pre>
     *
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param keyboard the given {@link IbmPcKeyboard keyboard} instance
     */
    private void getKeyboardStatus(final I8086 cpu, final IbmPcKeyboard keyboard) {
        // are keystrokes waiting?
        final boolean keystrokesWaiting = keyboard.keyStrokesWaiting();

        // if a scan code is waiting ...
        if (keystrokesWaiting) {
            // peek at the next key in the buffer
            final int key = keyboard.peekAtNextKeyStore();
            cpu.AH.set(0);
            cpu.AL.set(key);
        }
        // no scan code is waiting ...
        else {
            cpu.AX.set(0);
        }

        // set registers and flags
        cpu.FLAGS.setZF(!keystrokesWaiting);
    }

    /**
     * <pre>
     * INT 16,2 - Read Keyboard Flags
     * AH = 02
     * on return:
     *  AL or BIOS Data Area 40:17
     * 	AL = BIOS keyboard flags (located in ~BIOS Data Area~ 40:17)
     * 		7 6 5 4 3 2 1 0
     * 		| | | | | | | |
     * 		| | | | | | | +---- right shift key depressed
     * 		| | | | | | +----- left shift key depressed
     * 		| | | | | +------ CTRL key depressed
     * 		| | | | +------- ALT key depressed
     * 		| | | +-------- scroll-lock is active
     * 		| | +--------- num-lock is active
     * 		| +---------- caps-lock is active
     * 		+----------- insert is active
     * </pre>
     *
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param keyboard the given {@link IbmPcKeyboard keyboard} instance
     */
    private void readKeyboardFlags(final I8086 cpu, final IbmPcKeyboard keyboard) {
        // put the key flags in AL
        cpu.AL.set(keyboard.getKeyFlags());
    }

    private void setTypematicRate(final I8086 cpu) {

    }

    private void setKeyboardClickAdjustment(final I8086 cpu) {

    }

}
