package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * DAS
 *
 * @author lawrence.daniels@gmail.com
 */
public class DAS extends AbstractOpCode {
    private static DAS instance = new DAS();

    /**
     * Private constructor
     */
    private DAS() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static DAS getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {

    }

}
