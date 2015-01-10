package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Clear Interrupt Flag
 *
 * Usage:  CLI
 * Modifies flags: IF
 *
 * Sets the Interrupt Flag to 0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CLI extends AbstractOpCode {
    private static CLI instance = new CLI();

    /**
     * Private constructor
     */
    private CLI() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CLI getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {
        cpu.FLAGS.setIF(false);
    }

}
