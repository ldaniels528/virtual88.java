package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Set Carry Flag
 *
 * Usage:  STC
 * Modifies flags: CF
 *
 * Sets the Carry Flag to 1.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class STC extends AbstractFlagUpdateOpCode {
    private static final STC instance = new STC();

    /**
     * Private constructor
     */
    private STC() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static STC getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.setCF(true);
    }

}
