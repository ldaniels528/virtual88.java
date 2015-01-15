package org.ldaniels528.javapc.jbasic.gwbasic;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyConstants;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemImpl;
import org.ldaniels528.javapc.jbasic.common.JBasicDisplayWrapper;
import org.ldaniels528.javapc.jbasic.common.JBasicKeyLabels;
import org.ldaniels528.javapc.jbasic.common.JBasicMemoryManager;
import org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicProgram;
import org.ldaniels528.javapc.jbasic.gwbasic.storage.GwBasicStorageDevice;

import java.awt.event.KeyEvent;

import static org.ldaniels528.javapc.jbasic.common.JBasicDisplayModes.MODE0b;

/**
 * BASICA/GWBASIC-based System Environment
 *
 * @author lawrence.daniels@gmail.com
 * @see org.ldaniels528.javapc.jbasic.common.JBasicDisplayWrapper
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicProgram
 */
public class GwBasicEnvironment extends IbmPcSystemImpl {
    public static final int PROGRAM_SEGEMENT = 0x13F0;
    private final JBasicDisplayWrapper displayWrapper;
    private final MemoryManager memoryManager;
    private final JBasicKeyLabels keyLabels;
    private final GwBasicProgram program;
    private final GwBasicStorageDevice disk;

    ///////////////////////////////////////////////////////////////////
    //			Constructor(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * Creates an instance of this {@link org.ldaniels528.javapc.ibmpc.system.IbmPcSystem environment}
     *
     * @param frame the given {@link IbmPcDisplayFrame frane}
     */
    public GwBasicEnvironment(final IbmPcDisplayFrame frame) {
        super(frame, IBM_PCjr);
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