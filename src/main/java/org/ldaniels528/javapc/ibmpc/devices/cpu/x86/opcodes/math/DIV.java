/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * <h4>Unsigned divide</h4>
 * <pre>
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
 * 	MOV AX, 203   ; AX = 00CBh
 * 	MOV BL, 4
 * 	DIV BL        ; AL = 50 (32h), AH = 3
 * 	RET
 * </pre>
 * op8: 8-bit register or memory
 * op16: 16-bit register or memory
 * Action: If operand is op8, unsigned AL = AX / op8  and  AH = AX % op8
 * If operand is op16, unsigned AX = DX::AX / op16  and  DX = DX::AX % op16
 * Flags Affected: OF=?, SF=?, ZF=?, AF=?, PF=?, CF=?
 * Notes: Performs both division and modulus operations in one instruction.
 * @author lawrence.daniels@gmail.com
 */
@FlagsAffected({"AF", "CF", "OF", "PF", "SF", "ZF"})
public class DIV extends AbstractOpCode {
    private final Operand operand;

    /**
     * DIV <i>operand</i>
     *
     * @param operand the given {@link Operand operand}
     */
    public DIV(final Operand operand) {
        this.operand = operand;
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(IbmPcSystem system, final I8086 cpu)
            throws X86AssemblyException {
        final int value0;
        final int value1;

        switch (operand.size()) {
            // is it operand 8-bit?
            case SIZE_8BIT:
                // get the divisor and operand
                value0 = cpu.AX.get();
                value1 = operand.get();

                // put the values in AL and AH
                if (value1 != 0) {
                    cpu.AL.set(value0 / value1);
                    cpu.AH.set(value0 % value1);
                } else {
                    // call interrupt 0
                    cpu.execute(system, INT.DIVBYZR);
                }
                break;

            // is it operand 16-bit?
            case SIZE_16BIT:
                // get the divisor and operand
                value0 = (cpu.DX.get() << 16) | cpu.AX.get();
                value1 = operand.get();

                // put the values in AL and AH
                if (value1 != 0) {
                    cpu.AL.set(value0 / value1);
                    cpu.AH.set(value0 % value1);
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