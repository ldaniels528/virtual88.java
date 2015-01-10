/**
 *
 */
package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

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
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {
        cpu.FLAGS.setCF(false);
    }

}