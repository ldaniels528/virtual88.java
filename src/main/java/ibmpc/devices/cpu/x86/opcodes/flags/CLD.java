package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.system.IbmPcSystem;

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
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        cpu.FLAGS.setDF(false);
    }

}
