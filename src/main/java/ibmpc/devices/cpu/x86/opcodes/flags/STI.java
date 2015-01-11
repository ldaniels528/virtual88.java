package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Set Interrupt Flag
 *
 * Usage:  STI
 * Modifies flags: IF
 *
 * Sets the Interrupt Flag to 1.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class STI extends AbstractOpCode {
    private static STI instance = new STI();

    /**
     * Private constructor
     */
    private STI() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static STI getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        cpu.FLAGS.setIF(true);
    }

}
