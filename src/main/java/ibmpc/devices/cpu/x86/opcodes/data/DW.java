package ibmpc.devices.cpu.x86.opcodes.data;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Define Word (DW) MACRO Instruction
 *
 * @author ldaniels
 */
public class DW extends AbstractOpCode {
    private int value;

    /**
     * Creates a new define word instruction
     *
     * @param wordValue the given word value
     */
    public DW(final int wordValue) {
        this.value = wordValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("DW %04X", value);
    }

}
