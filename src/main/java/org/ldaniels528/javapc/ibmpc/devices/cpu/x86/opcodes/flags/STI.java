package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Set Interrupt Flag
 *
 * Usage:  STI
 * Modifies flags: IF
 *
 * Sets the Interrupt Flag to 1.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class STI extends AbstractFlagUpdateOpCode {
    private static final STI instance = new STI();

    /**
     * Private constructor
     */
    private STI() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static STI getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.setIF(true);
    }

}
