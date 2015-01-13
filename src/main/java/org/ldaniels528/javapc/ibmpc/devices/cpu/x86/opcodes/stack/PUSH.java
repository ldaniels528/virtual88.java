package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Stack;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 *  Usage:  PUSH    src
 *          PUSH    immed   (80188+ only)
 *  Modifies flags: None
 *
 *  Decrements SP by the size of the operand (two or four, byte values
 *  are sign extended) and transfers one word from source to the stack
 *  top (SS:SP).
 *
 *                           Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *
 *  reg16           11/15   3     2     1             1
 *  reg32             -     -     2     1             1
 *  mem16           16+EA   5     5     4            2-4  (W88=24+EA)
 *  mem32             -     -     5     4            2-4
 *  segreg          10/14   3     2     3             1
 *  immed             -     3     2     1            2-3
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class PUSH extends AbstractOpCode {
    private final Operand operand;

    /**
     * Creates a new PUSH instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public PUSH(final Operand operand) {
        this.operand = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        // get the stack instance
        final X86Stack stack = cpu.getStack();

        // push the operand onto the stack
        stack.pushValue(operand.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("PUSH %s", operand);
    }

}