package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
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
public class STC extends AbstractOpCode {
    private static STC instance = new STC();

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

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        cpu.FLAGS.setCF(true);
    }

}
