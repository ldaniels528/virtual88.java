package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Clear Interrupt Flag
 *
 * Usage:  CLI
 * Modifies flags: IF
 *
 * Sets the Interrupt Flag to 0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CLI extends AbstractFlagUpdateOpCode {
    private static final CLI instance = new CLI();

    /**
     * Private constructor
     */
    private CLI() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CLI getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.setIF(false);
    }

}
