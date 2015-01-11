package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios;

import org.apache.log4j.Logger;
import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryAddressFAR32;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.*;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemInfo;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.ldaniels528.javapc.ibmpc.devices.display.fonts.IbmPcFont8x8.FONTS_8X8;

/**
 * Represents the memory resident portion of the
 * Basic Input/Output System (BIOS) for an
 * IBM PC/XT/AT Systems.
 * <pre>
 * Sets up the expected ROM values:
 * 	Address	   Size Contents
 *  ---------  ----	---------
 *  0040:0000	2  	Base port address of first RS-232 adapter (COM1) See COM Ports
 *  0040:0002	2  	Port of COM2
 *  0040:0004	2  	Port of COM3
 *  0040:0006	2 	Port of COM4
 *  0040:0008	2 	Base port addr of first parallel printer (LPT1)  Printer Ports
 *  0040:000A	2  	Port of LPT2
 *  0040:000C	2  	Port of LPT3
 *  0040:000E	2  	Port of LPT4
 *  0040:0010	2  	Equipment/hardware installed/active;
 *                {@link org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.EquipmentListServices see Equipment List}
 *  0040:0012   1  	Errors in PCjr infrared keyboard link
 *  0040:0013   2  	Total memory in K-bytes (same as obtained via INT 12H)
 *  0040:0015   2  	Scratch pad for manufacturing error tests
 *  0040:0017   2  	Keyboard status bits; see Keyboard Shift Status Flags
 *  0040:0019   1  	Current (accumulating) value of Alt+numpad pseudo-key input;
 *      		   	normally 0.  When [Alt] is released, value is stored in
 *      		   	keyboard buffer at 001E.
 *  0040:001a   2  	Address of keyboard buffer head (keystroke at that address is next)
 *  0040:001c   2  	Address of keyboard buffer tail
 *  0040:001e  	32 	Keyboard buffer.  BIOS stores keystrokes here (head and tail
 *      		   	point to addresses from 041EH to 043DH inclusive).
 *  0040:003e   1  	Diskette drive needs recalibration (bit 0=A, bit 1=B, etc.)
 *     	 		   	bits 4-5 indicate which drive is currently selected
 *  0040:003f   1  	Diskette motor is running (bit 0=drive A, bit 1=B, etc.)
 *  0040:0040   1  	Time until motor off. INT 08h turns motor off when this is 0.
 *  0040:0041   1  	Diskette error status; same as status returned by INT 13H
 *  0040:0042   7  	Diskette controller status information area
 *  0040:0049   1  	Current active video mode.  See Video Modes and INT 10H.
 *  0040:004a   2  	Screen width in text columns
 *  0040:004c   2  	Length (in bytes) of video area (regen size)
 *  0040:004e   2  	Offset from video segment of active video memory page
 *  0040:0050  	16 	Cursor location (8 byte-pairs; low byte=clm, hi byte=row)
 *  0040:0060   2  	Cursor size/shape.  Low byte=end scan line; hi byte=start line.
 *  0040:0062   1  	Current active video page number
 *  0040:0063   2  	Port address for 6845 video controller chip; see CGA I/O Ports
 *  0040:0065   1  	Current value of 6845 video ctrlr CRT_MODE (port 3x8H register)
 *  0040:0066   1  	Current value of 6845 video ctrlr CRT_PALETTE (port 3x9H reg)
 *  0040:0067   5  	Cassette data area or POST data area
 *  0040:006C   4  	Timer tick counter (count of 55ms ticks since CPU reset)
 *  0040:0070   1  	Timer overflow flag (timer has rolled over 24 hr)
 *  0040:0071   1  	Ctrl-Break flag.  Bit 7=1 when break was pressed.
 *  				This never gets reset unless you do it yourself.
 *  0040:0072   2  	1234h means Ctrl+Alt+Del reboot is in progress.  BIOS checks
 *     				this to avoid doing a "cold boot" with the time-consuming POST
 *     				4321h means reset, preserving memory
 *     				5678h, 9ABCh, and ABCDh (are internal PC Convertible codes)
 *  0040:0074   4  	PCjr diskette or AT hard disk control area
 * 		(0074)	1 	Status of last fixed-disk drive operation
 * 		(0075)	1 	Number of hard disk drives for AT
 * 		(0077)	1 	Hard disk port for XT.  See XT Hard Disk Ports.
 *  0040:0078   4  	Printer time-out values (478H=Lpt1, 478H=Lpt2...)
 *  0040:007c   4  	RS-232 time-out values  (47cH=Com1, 47dH=Com2...)
 *  0040:0080   2  	AT PS/2 keyboard buffer offset start address (usually 01eH)
 *  0040:0082   2                                   end address (usually 003eH)
 *  0040:0084   1  	EGA text rows-1  (maximum valid row value)
 *  0040:0085   2  	EGA bytes per character (scan-lines/char used in active mode)
 *  0040:0087   1  	EGA flags; see EgaMiscInfoRec
 *  0040:0088   1  	EGA flags; see EgaMiscInfo2Rec
 *  0040:0089   1  	VGA flags; see VgaFlagsRec (See also:  EGA/VGA Data Areas)
 *  0040:008b   1  	AT PS/2 Media control: data rate, step rate
 *  0040:008c   1  	AT PS/2 Hard disk drive controller status
 *  0040:008d   1  	AT PS/2 Hard disk drive error status
 *  0040:008e   1  	AT PS/2 Hard disk drive interrupt control
 *  0040:0090   1  	AT PS/2 Disk media state bits for drive 0
 *  0040:0091   1                                for drive 1
 *  0040:0092   1  	AT PS/2 Disk operation started flag for drive 0
 *  0040:0093   1                                	   for drive 1
 *  0040:0094   1  	AT PS/2 Present cylinder number for drive 0
 *  0040:0095   1                                  for drive 1
 *  0040:0096   1  	AT Keyboard flag bit 4=1 (10H) if 101-key keyboard is attached
 *  0040:0097   1  	AT Keyboard flag for LED 'key lock' display
 *     					bits 0-2 are ScrollLock, NumLock, CapsLock
 *  0040:0098   4  	AT Pointer to 8-bit user wait flag; see INT 15H 86H
 *  0040:009c   4  	AT Microseconds before user wait is done
 *  0040:00a0   1  	AT User wait activity flag:
 *       				01h=busy, 80h=posted, 00h=acknowledged
 *  0040:00a1   7  	AT Reserved for network adapters
 *  0040:00a8   4  	EGA Address of table of pointers; see EgaSavePtrRec
 *  0040:00f0  	16	(IAC) Inter-Aapplication Communication area.  Programs may use
 *     				this area to store status, etc.  Might get overwritten by
 *     				another program.
 *  0040:0100   1  	Print-screen status:
 *       				00h=ok; 01h=printing; 0FFh=error while printing screen
 *  0040:0104   1  	Phantom-floppy status;  see also DOS fn 440eH
 *       				01h=drive A is acting as drive B.
 *  0040:0110  	17 	Used by BASIC interpreter
 *  0040:0130   3  	Used by MODE command
 *  	.
 *  	.
 *  f000:0000	2	TODO Temporarily using for the BIOS interrupt handler
 *  	.
 *  	.
 *  f000:fff0 	5  	FAR JMP instruction to begin POST
 *     				(after a hard reset, the CPU jumps here)
 * 	f000:fff5	8  	ROM-BIOS release date in ASCII (eg, "04/24/81" is original PC)
 * 	f000:fffc 	2  	(unused)
 * 	f000:fffe 	1  	IBM computer-type code (See {@link IbmPcSystem})
 * </pre>
 *
 * @author ldaniels
 */
public class IbmPcBIOS {
    // define the Interrupt Request Lines (IRQ)
    public static final INT IRQ_0 = new INT(0x08);    // System Timer
    public static final INT IRQ_1 = new INT(0x09);    // Keyboard
    public static final INT IRQ_2 = new INT(0x0A);    // Reserved
    public static final INT IRQ_3 = new INT(0x0B);    // COM2:
    public static final INT IRQ_4 = new INT(0x0C);    // COM1:
    public static final INT IRQ_5 = new INT(0x0D);    // LPT2:
    public static final INT IRQ_6 = new INT(0x0E);    // Floppy
    public static final INT IRQ_7 = new INT(0x0F);    // LPT1:
    public static final INT IRQ_8 = new INT(0x70);    // Real Time Clock (RTC)
    public static final INT IRQ_9 = new INT(0x71);    // IRQ2 redirection
    public static final INT IRQ_A = new INT(0x72);    // Reserved
    public static final INT IRQ_B = new INT(0x73);    // Reserved
    public static final INT IRQ_C = new INT(0x74);    // Available | PS/2 Mouse
    public static final INT IRQ_D = new INT(0x75);    // Math coprocessor
    public static final INT IRQ_E = new INT(0x76);    // Primary IDE HDD
    public static final INT IRQ_F = new INT(0x77);    // Available | Seconary IDE HDD

    // define miscellaneous constants
    private static final byte[] ROM_DATE = "04/24/1981".getBytes();

    // internal fields
    private final Logger logger = Logger.getLogger(getClass());
    private Map<MemoryAddressFAR32, InterruptHandler> handlers;
    private final IbmPcRandomAccessMemory memory;

    /**
     * Default constructor
     */
    public IbmPcBIOS(final IbmPcRandomAccessMemory memory) {
        this.memory = memory;
        this.handlers = new HashMap<MemoryAddressFAR32, InterruptHandler>();

        // update the vector table
        createVectorTable();

        // register the BIOS services
        register(0x00, 0xF000, 0x0000, DivisionByZeroInterrupt.getInstance());
        register(0x05, 0xF000, 0x0004, PrintScreenService.getInstance());
        register(0x08, 0xF000, 0x0008, SystemTimerService.getInstance());
        register(0x06, 0xF000, 0x000C, InvalidOpCodeInterrupt.getInstance());
        register(0x09, 0xF000, 0x0010, KeyboardHardwareServices.getInstance());
        register(0x10, 0xF000, 0x0014, VideoServices.getInstance());
        register(0x11, 0xF000, 0x0018, EquipmentListServices.getInstance());
        register(0x12, 0xF000, 0x001C, ConventionalMemorySizeService.getInstance());
        register(0x13, 0xF000, 0x0020, DiskServices.getInstance());
        register(0x14, 0xF000, 0x0024, SerialPortServices.getInstance());
        register(0x15, 0xF000, 0x0028, ATSystemServices.getInstance());
        register(0x16, 0xF000, 0x002C, KeyboardSoftwareServices.getInstance());
        register(0x17, 0xF000, 0x0030, ParallelPortServices.getInstance());
        register(0x19, 0xF000, 0x0034, SystemRebootService.getInstance());
        register(0x1A, 0xF000, 0x0038, RealTimeClockServices.getInstance());

        // setup the address of each interrupt
        //showVectorTable();
    }

    /**
     * Returns the Random Access Memory (RAM) instance
     *
     * @return the given {@link IbmPcRandomAccessMemory memory} instance
     */
    public IbmPcRandomAccessMemory getMemory() {
        return memory;
    }

    /**
     * Handles the given interrupt number
     *
     * @param system    the given {@link org.ldaniels528.javapc.ibmpc.system.IbmPcSystem IBM PC system}
     * @param cpu       the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
     * @param interrupt the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.INT interrupt opCode}
     */
    public void invoke(final IbmPcSystem system, final Intel8086 cpu, final INT interrupt) throws X86AssemblyException {
        // get the interrupt #
        final int interruptNo = interrupt.getInterruptNumber();

        // get the vector address
        final MemoryAddressFAR32 address = getVectorAddress(interruptNo);

        // is there a system handler defined?
        if (handlers.containsKey(address)) {
            final InterruptHandler handler = handlers.get(address);
            logger.info(format("Handling interrupt %02X with %s (%s)", interruptNo, handler.getClass().getSimpleName(), address));

            // handle the interrupt
            handler.process(system, cpu);
        }

        // otherwise, use the defined user handler
        else {
            logger.info(format("Handling interrupt %02X at %s", interruptNo, address));

            // jump to the address in memory
            cpu.invokeInterrupt(address);
        }
    }

    /**
     * Registers the given service as the handler
     * for the given interrupt number
     *
     * @param interruptNo the given interrupt number
     * @param handler     the given {@link InterruptHandler interrupt handler}
     */
    public void register(final int interruptNo,
                         final int segment,
                         final int offset,
                         final InterruptHandler handler) {
        // lookup the memory address
        final MemoryAddressFAR32 address = getVectorAddress(interruptNo);

        // get the interrupt segment and offset values
        final int baseSegment = address.getSegment();
        final int baseOffset = address.getOffset();

        // set offset:segment value
        memory.setWord(baseSegment, baseOffset, offset);
        memory.setWord(baseSegment, baseOffset + 2, segment);

        // create a mapping between the address and handler
        handlers.put(address, handler);
    }

    /**
     * Returns the offset and segment of the interrupt vector for
     * the given interrupt number
     *
     * @param interruptNumber the given interrupt number
     * @return an integer array containing the the offset and segment
     */
    public int[] getInterruptVector(final int interruptNumber) {
        final int dstSegment = 0x0000;
        final int dstOffset = (interruptNumber * 4);
        final int offset = memory.getWord(dstSegment, dstOffset);
        final int segment = memory.getWord(dstSegment, dstOffset + 2);
        return new int[]{offset, segment};
    }

    /**
     * Updates a single entry in the Interrupt vector table
     *
     * @param interruptNumber the given interrupt to update
     * @param segment         the vector segment address
     * @param offset          the vector offset address
     */
    public void setInterruptVector(final int interruptNumber, final int segment, final int offset) {
        final int dstSegment = 0x0000;
        final int dstOffset = (interruptNumber * 4);
        memory.copyBytes(segment, offset, dstSegment, dstOffset, 4);
    }

    /**
     * Updates the key status (CTRL, ALT, SHIFT, etc) area of BIOS memory
     *
     * @param keyFlags the given key flags as an integer bit array
     */
    public void updateKeyFlags(final int keyFlags) {
        memory.setByte(0x0040, 0x0017, keyFlags);
    }

    /**
     * Updates the the system information area of BIOS memory
     *
     * @param systemInfo the {@link IbmPcSystemInfo system information}
     * @see IbmPcSystem
     */
    public void updateSystemInfo(final IbmPcSystemInfo systemInfo) {
        // Conventional memory size (640K)
        memory.setWord(0x0040, 0x0013, memory.sizeInKilobytes());

        // Print-screen status: 00h=ok; 01h=printing; 0FFh=error while printing screen
        memory.setByte(0x0040, 0x0100, 0);

        // Phantom-floppy status: 01h=drive A is acting as drive B.
        memory.setByte(0x0040, 0x0104, (byte) (systemInfo.getFloppyDrives() == 1 ? 1 : 0));

        // ROM-BIOS release date in ASCII
        memory.setBytes(0xF000, 0xFFF5, ROM_DATE, ROM_DATE.length);

        // set the computer code
        memory.setByte(0xF000, 0xFFFE, (byte) systemInfo.getSystemType());
    }

    /**
     * Updates the video information area of BIOS memory
     *
     * @param mode the given {@link IbmPcDisplayMode display mode}
     * @param dc   the given {@link IbmPcDisplayContext display context}
     */
    public void updateVideoInfo(final IbmPcDisplayMode mode, final IbmPcDisplayContext dc) {
        // set the current active video mode.
        memory.setByte(0x0040, 0x0049, (byte) mode.getVideoMode());

        // set the column width in memory
        memory.setByte(0x0040, 0x004A, (byte) mode.getColumns());

        // set the offset from video segment of active video memory page
        memory.setWord(0x0040, 0x004E, dc.activePage * mode.getPageSize());

        // set the cursor location (8 byte-pairs; low byte=clm, hi byte=row)
        memory.setWord(0x0040, 0x0050, dc.position);

        // set the cursor size/shape.  Low byte=end scan line; hi byte=start line.
        memory.setWord(0x0040, 0x0060, 0x0607); // TODO Fix this!

        // set the current active video page number
        memory.setByte(0x0040, 0x0062, (byte) dc.activePage);

        // set the port address for 6845 video controller chip
        memory.setWord(0x0040, 0x0063, 0x03D4); // TODO Fix this!

        // set the current value of 6845 video controller CRT_MODE
        memory.setWord(0x0040, 0x0065, 0x3029); // TODO Fix this!

        // set the current value of 6845 video controller
        memory.setWord(0x0040, 0x0066, 0x0030); // TODO Fix this!

        // set the 8x8 fonts
        memory.setBytes(0xC000, 0x2AE6, FONTS_8X8, FONTS_8X8.length);
    }

    /**
     * Sets up the Interrupt vector table
     */
    private void createVectorTable() {
        // create the default vectors
        createDefaultVectorTableEntries();

        // vector 1F points to ASCII characters 128 thru 255 [at C000:2520]
        // TODO set this up
        //memory.setBytes( 0xC000, 0x2520, null );
    }

    /**
     * Creates the Interrupt vector table
     * (256 x 4-byte values from 0000:0000 to 0000:03FC)
     */
    private void createDefaultVectorTableEntries() {
        // define the vector table segment and offset
        final int baseSegment = 0x0000;

        // define the number of interrupts
        final int interrupts = 256; // 100h

        // define the default vector segment and offset
        final int defaultSegment = 0xF000;
        final int defaultOffset = 0x1000;

        // setup the address of each interrupt
        for (int n = 0; n < interrupts; n++) {
            // setup the offset
            final int offset = (n * 4);

            // set offset:segment value
            memory.setWord(baseSegment, offset, defaultOffset);
            memory.setWord(baseSegment, offset + 2, defaultSegment);
        }

        // place an IRET instruction at the default vector address
        memory.setByte(defaultSegment, defaultOffset, (byte) 0xCF);
    }

    /**
     * Get the vector address for this interrupt
     *
     * @param interrupt the given interrupt number
     * @return the segment and offset
     */
    private MemoryAddressFAR32 getVectorAddress(final int interrupt) {
        // get the segment and offset for this vector
        final int interruptOffset = interrupt * 4;

        // return the segment:offset
        return new MemoryAddressFAR32(memory, 0x0000, interruptOffset);
    }

    /**
     * Displays the interrupt vector table
     */
    protected void showVectorTable() {
        final int segment = 0x0000;
        for (int n = 0; n < 256; n++) {
            // setup the offset
            final int offset = (n * 4);

            // get the memory address
            final int pointer = memory.getDoubleWord(segment, offset);

            // get the code segment and offset
            final int codeOffset = (pointer & 0xFFFF0000) >> 16;
            final int codeSegment = (pointer & 0x0000FFFF);

            // set offset:segment value
            logger.info(format("Interrupt %02X [at %04X:%04X] points to %04X:%04X (%08X)", n, segment, offset, codeSegment, codeOffset, pointer));

        }
    }

}
