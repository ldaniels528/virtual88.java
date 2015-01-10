package ibmpc.devices.cpu.x86.opcodes.bitwise.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * BT -- Bit Test
 * <pre>
 * Opcode         Instruction     Clocks    Description
 * 0F  A3         BT r/m16,r16    3/12      Save bit in carry flag
 * 0F  A3         BT r/m32,r32    3/12      Save bit in carry flag
 * 0F  BA /4 ib   BT r/m16,imm8   3/6       Save bit in carry flag
 * 0F  BA /4 ib   BT r/m32,imm8   3/6       Save bit in carry flag
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class BT extends AbstractDualOperandOpCode {

    /**
     * Default constructor
     */
    public BT(final Operand dest, final Operand src) {
        super("BT", dest, src);
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        throw new IllegalStateException("Not yet implemented");
    }

}
