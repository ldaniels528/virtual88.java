package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
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
public class CLI extends AbstractOpCode {
    private static CLI instance = new CLI();

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

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        cpu.FLAGS.setIF(false);
    }

}
