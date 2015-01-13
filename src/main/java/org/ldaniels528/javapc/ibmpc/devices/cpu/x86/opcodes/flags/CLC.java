/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Clear Carry Flag
 *
 * Usage:  CLC
 * Modifies flags: CF
 *
 * Sets the Carry Flag to 0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CLC extends AbstractOpCode {
    private static CLC instance = new CLC();

    /**
     * Private constructor
     */
    private CLC() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CLC getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.setCF(false);
    }

}