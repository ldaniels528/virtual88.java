package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.stack;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Stack;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractSingleOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.RegistersAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

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
@RegistersAffected({"SP"})
public class POP extends AbstractSingleOperandOpCode {

    /**
     * Creates a new POP instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public POP(final Operand operand) {
        super("POP", operand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        // get the stack instance
        final X86Stack stack = cpu.getStack();

        // push the operand onto the stack
        operand.set(stack.popValue());
    }

}