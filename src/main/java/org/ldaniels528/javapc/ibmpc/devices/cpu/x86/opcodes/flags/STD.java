package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Set Direction Flag
 *
 * Usage:  STD
 * Modifies flags: DF
 *
 * Sets the Direction Flag to 1.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class STD extends AbstractOpCode {
    private static STD instance = new STD();

    /**
     * Private constructor
     */
    private STD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static STD getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.setDF(true);
    }

}
