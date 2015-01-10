package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * DEC <i>target</i>
 *
 * @author lawrence.daniels@gmail.com
 */
public class DEC extends AbstractOpCode {
    private final Operand operand;

    /**
     * Creates a new increment instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public DEC(final Operand operand) {
        this.operand = operand;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        final int value = operand.get();
        operand.set(value - 1);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return String.format("DEC %s", operand);
    }

}