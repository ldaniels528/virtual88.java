package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * <pre>
 * Signed divide.
 *
 * Algorithm:
 *
 * 	when operand is a byte:
 * 		AL = AX / operand
 * 		AH = remainder (modulus)
 *
 * 	when operand is a word:
 * 		AX = (DX AX) / operand
 * 		DX = remainder (modulus)
 *
 * Example:
 * 	MOV AX, -203 ; AX = 0FF35h
 * 	MOV BL, 4
 * 	IDIV BL      ; AL = -50 (0CEh), AH = -3 (0FDh)
 * 	RET
 * </pre>
 * See also <a href="http://home.comcast.net/~fbui/intel/i.html#idiv"">IDIV</a>
 *
 * @author lawrence.daniels@gmail.com
 */
public class IDIV extends AbstractOpCode {
    private final Operand operand;

    /**
     * IDIV <i>operand</i>
     *
     * @param operand the given {@link Operand operand}
     */
    public IDIV(final Operand operand) {
        this.operand = operand;
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        switch (operand.size()) {
            // is it operand 8-bit?
            case SIZE_8BIT:
                // get the divisor and operand
                final short valueA0 = (short) cpu.AX.get();
                final byte valueA1 = (byte) operand.get();

                // put the values in AL and AH
                if (valueA1 != 0) {
                    cpu.AL.set(valueA0 / valueA1);
                    cpu.AH.set(valueA0 % valueA1);
                } else {
                    // call interrupt 0
                    cpu.execute(system, INT.DIVBYZR);
                }
                break;

            // is it operand 16-bit?
            case SIZE_16BIT:
                // get the divisor and operand
                final int valueB0 = (cpu.DX.get() << 16) | cpu.AX.get();
                final short valueB1 = (short) operand.get();

                // put the values in AL and AH
                if (valueB1 != 0) {
                    cpu.AL.set(valueB0 / valueB1);
                    cpu.AH.set(valueB0 % valueB1);
                } else {
                    // call interrupt 0
                    cpu.execute(system, INT.DIVBYZR);
                }
                break;

            // unhandled
            default:
                throw new X86AssemblyException("Unhandled operand size " + operand.size());
        }
    }

}