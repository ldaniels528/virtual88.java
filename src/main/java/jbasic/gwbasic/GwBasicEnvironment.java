package jbasic.gwbasic;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.keyboard.IbmPcKeyConstants;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.MemoryManager;
import ibmpc.system.IbmPcSystemXT;
import jbasic.common.JBasicDisplayWrapper;
import jbasic.common.JBasicKeyLabels;
import jbasic.common.JBasicMemoryManager;
import jbasic.common.program.JBasicSourceCode;
import jbasic.gwbasic.program.GwBasicProgram;
import jbasic.gwbasic.storage.GwBasicStorageDevice;

import java.awt.event.KeyEvent;

import static jbasic.common.JBasicDisplayModes.MODE0b;

/**
 * BASICA/GWBASIC-based System Environment
 *
 * @author lawrence.daniels@gmail.com
 * @see jbasic.common.JBasicDisplayWrapper
 * @see jbasic.gwbasic.program.GwBasicProgram
 */
public class GwBasicEnvironment extends IbmPcSystemXT {
    public static final int PROGRAM_SEGEMENT = 0x1000;
    private final JBasicDisplayWrapper displayWrapper;
    private final MemoryManager memoryManager;
    private final JBasicKeyLabels keyLabels;
    private final GwBasicProgram program;
    private final GwBasicStorageDevice disk;

    ///////////////////////////////////////////////////////////////////
    //			Constructor(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * Creates an instance of this {@link ibmpc.system.IbmPcSystem environment}
     *
     * @param frame the given {@link IbmPcDisplayFrame frane}
     */
    public GwBasicEnvironment(final IbmPcDisplayFrame frame) {
        super(frame);
        this.keyLabels = new JBasicKeyLabels();
        this.displayWrapper = new JBasicDisplayWrapper(display, MODE0b, keyLabels);
        this.program = new GwBasicProgram(this);
        this.memoryManager = new JBasicMemoryManager(memory, PROGRAM_SEGEMENT);
        this.disk = new GwBasicStorageDevice();
    }

    ///////////////////////////////////////////////////////////////////
    //			Service Method(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcDisplay getDisplay() {
        return displayWrapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GwBasicStorageDevice getStorageSystem() {
        return disk;
    }

    /**
     * Returns the currently loaded memory manager
     *
     * @return the currently loaded {@link MemoryManager memory manager}
     */
    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    /**
     * Returns the current memory resident program
     *
     * @return the current memory resident {@link JBasicSourceCode program}
     */
    public GwBasicProgram getProgram() {
        return program;
    }

    ///////////////////////////////////////////////////////////////////
    //			Keyboard Input-related Method(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * Turns the Function Key Display On/Off
     *
     * @param active the determinant of whether the state is on or off
     */
    public void keyLabelsDisplayed(final boolean active) {
        keyLabels.setActive(active);
    }

    /**
     * Sets the label for the given key index
     *
     * @param index the given key index (1-10)
     * @param label the given label
     */
    public void setKeyLabel(final int index, final String label) {
        keyLabels.setLabel(index - 1, label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final IbmPcKeyboard keyboard, final KeyEvent e) {
        // get the key code
        final int keyCode = e.getKeyCode();

        // is it F1 .. F10?
        if ((keyCode >= KeyEvent.VK_F1) && (keyCode <= KeyEvent.VK_F10)) {
            // determine which function key was pressed
            final int index = (keyCode - KeyEvent.VK_F1);

            // get the key label for the function key
            final String keyLabel = keyLabels.getLabel(index);

            // check to see if enter should be called as well
            if (keyLabel.charAt(keyLabel.length() - 1) == IbmPcKeyConstants.ESC) {
                keyboard.sendKeyStrokes(keyLabel.substring(0, keyLabel.length() - 1));
                keyboard.sendEnterKey();
            } else {
                keyboard.sendKeyStrokes(keyLabel);
            }
        }
    }

}