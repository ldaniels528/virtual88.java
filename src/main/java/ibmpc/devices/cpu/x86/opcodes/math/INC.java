package ibmpc.devices.cpu.x86.opcodes.math;


import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * INC
 *
 * @author lawrence.daniels@gmail.com
 */
public class INC extends AbstractOpCode {
    private final Operand operand;

    /**
     * Creates a new increment instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public INC(final Operand operand) {
        this.operand = operand;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        final int value = operand.get();
        operand.set(value + 1);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return String.format("INC %s", operand);
    }

}
