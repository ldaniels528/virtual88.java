package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;


import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * <pre>
 * Signed multiply.
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
 * 		MOV AL, -2
 * 		MOV BL, -4
 * 		IMUL BL      ; AX = 8
 * 		RET
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class IMUL extends AbstractOpCode {
    private final Operand operand;

    /**
     * IMUL <i>operand</i>
     *
     * @param operand the given {@link Operand operand}
     */
    public IMUL(final Operand operand) {
        this.operand = operand;
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(IbmPcSystem system, final Intel8086 cpu)
            throws X86AssemblyException {
        switch (operand.size()) {
            // is it operand 8-bit?
            case SIZE_8BIT:
                // get the divisor and operand
                final short value0_0 = (short) cpu.AL.get();
                final byte value0_1 = (byte) operand.get();

                // put the values in AL and AH
                cpu.AX.set(value0_0 * value0_1);
                break;

            // is it operand 16-bit?
            case SIZE_16BIT:
                // get the divisor and operand
                final int value1_0 = cpu.AX.get();
                final short value1_1 = (short) operand.get();

                // compute the product
                final int product = value1_0 * value1_1;

                // put the value in DX:AX
                cpu.DX.set((product & 0xFFFF0000) >> 16);
                cpu.AX.set(product & 0x0000FFFF);
                break;

            // unhandled
            default:
                throw new X86AssemblyException(String.format("Unhandled operand size %s", operand.size()));
        }
    }

}