/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * DAA
 *
 * @author lawrence.daniels@gmail.com
 */
public class DAA extends AbstractOpCode {
    private static DAA instance = new DAA();

    /**
     * Private constructor
     */
    private DAA() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static DAA getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel8086 cpu) {

    }

}
