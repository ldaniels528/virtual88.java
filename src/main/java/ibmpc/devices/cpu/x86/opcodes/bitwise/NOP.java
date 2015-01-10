package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * No Operation (NOP)
 *
 * @author lawrence.daniels@gmail.com
 */
public class NOP extends AbstractOpCode {
    private static NOP instance = new NOP();

    /**
     * Private constructor
     */
    private NOP() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static NOP getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        // do nothing
    }

}

