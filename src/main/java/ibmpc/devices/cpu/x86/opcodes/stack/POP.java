package ibmpc.devices.cpu.x86.opcodes.stack;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86Stack;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  POP     dest
 * Modifies flags: None
 *
 * Transfers word at the current stack top (SS:SP) to the destination
 * then increments SP by two to point to the new stack top.  CS is not
 * a valid destination.
 *
 *                          Clocks                 Size
 * Operands         808x  286   386   486          Bytes
 *
 * reg16             8     5     4     4             1
 * reg32             4     -     -     4             1
 * segreg            8     5     7     3             1
 * mem16           17+EA   5     5     6            2-4
 * mem32             5     -     -     6            2-4
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class POP extends AbstractOpCode {
    private final Operand operand;

    /**
     * Creates a new POP instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public POP(final Operand operand) {
        this.operand = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
        // get the stack instance
        final X86Stack stack = cpu.getStack();

        // push the operand onto the stack
        operand.set(stack.popValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("POP %s", operand);
    }

}