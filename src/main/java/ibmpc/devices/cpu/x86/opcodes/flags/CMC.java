package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Complement Carry Flag
 *
 * Usage:  CMC
 * Modifies flags: CF
 *
 * Toggles the Carry Flag from 0 to 1 or 1 to 0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CMC extends AbstractOpCode {
    private static CMC instance = new CMC();

    /**
     * Private constructor
     */
    private CMC() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CMC getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {
        cpu.FLAGS.setCF(!cpu.FLAGS.isCF());
    }

}
