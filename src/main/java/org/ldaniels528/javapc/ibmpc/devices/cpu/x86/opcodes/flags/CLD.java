package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Clear Direction Flag
 *
 * Usage:  CLD
 * Modifies flags: DF
 *
 * Sets the Direction Flag to 0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CLD extends AbstractOpCode {
    private static CLD instance = new CLD();

    /**
     * Private constructor
     */
    private CLD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CLD getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        cpu.FLAGS.setDF(false);
    }

}
