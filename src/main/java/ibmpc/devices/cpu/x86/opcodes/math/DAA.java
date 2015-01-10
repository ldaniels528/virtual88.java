/**
 *
 */
package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

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
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {

    }

}
