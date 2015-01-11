package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * <pre>
 * Unsigned multiply.
 *
 * Algorithm:
 *
 * 	when operand is a byte:
 * 		AX = AL * operand.
 *
 * 	when operand is a word:
 * 		(DX AX) = AX * operand.
 *
 * 	Example:
 * 		MOV AL, 200   ; AL = 0C8h
 * 		MOV BL, 4
 * 		MUL BL        ; AX = 0320h (800)
 * 		RET
 *
 * 		C	Z	S	O	P	A
 * 	 	r	?	?	r	?	?
 * 		CF=OF=0 when high section of the result is zero.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class MUL extends AbstractOpCode {
    private final Operand operand;

    /**
     * MUL <i>operand</i>
     *
     * @param operand the given {@link Operand operand}
     */
    public MUL(final Operand operand) {
        this.operand = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
        final int value0;
        final int value1;

        switch (operand.size()) {
            // is it operand 8-bit?
            case SIZE_8BIT:
                // get the multiplier and operand
                value0 = cpu.AL.get();
                value1 = operand.get();

                // put the values in AL and AH
                cpu.AX.set(value0 * value1);
                break;

            // is it operand 16-bit?
            case SIZE_16BIT:
                // get the multiplier and operand
                value0 = cpu.AX.get();
                value1 = operand.get();

                // compute the product
                final int product = value0 * value1;

                // put the value in DX:AX
                cpu.DX.set((product & 0xFFFF0000) >> 16);
                cpu.AX.set(product & 0x0000FFFF);
                break;

            // unhandled
            default:
                throw new X86AssemblyException("Unhandled operand size " + operand.size());
        }
    }

}