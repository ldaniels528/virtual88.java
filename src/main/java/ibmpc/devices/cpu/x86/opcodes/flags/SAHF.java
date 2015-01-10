package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * SAHF
 *
 * @author lawrence.daniels@gmail.com
 */
public class SAHF extends AbstractOpCode {
    private static SAHF instance = new SAHF();

    /**
     * Private constructor
     */
    private SAHF() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static SAHF getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {
        cpu.FLAGS.overlay(cpu.AH.get());
    }

}
