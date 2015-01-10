package ibmpc.devices.cpu.x86.opcodes.system.x186;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * ENTER -- Make Stack Frame for Procedure Parameters
 * <pre>
 * ENTER creates the stack frame required by most block-structured
 * high-level languages. The first operand specifies the number of
 * bytes of dynamic storage allocated on the stack for the routine
 * being entered. The second operand gives the lexical nesting
 * level (0 to 31) of the routine within the high-level language
 * source code. It determines the number of stack frame pointers
 * copied into the new stack frame from the preceding frame.
 * BP (or EBP, if the operand-size attribute is 32 bits) is the
 * current stack frame pointer.
 *
 * If the operand-size attribute is 16 bits, the processor uses BP
 * as the frame pointer and SP as the stack pointer. If the operand-size
 * attribute is 32 bits, the processor uses EBP for the frame pointer
 * and ESP for the stack pointer.
 *
 * If the second operand is 0, ENTER pushes the frame pointer (BP or EBP)
 * onto the stack; ENTER then subtracts the first operand from the stack
 * pointer and sets the frame pointer to the current stack-pointer value.
 *
 * For example, a procedure with 12 bytes of local variables would have an
 * ENTER 12,0 instruction at its entry point and a LEAVE instruction before
 * every RET. The 12 local bytes would be addressed as negative offsets from
 * the frame pointer.
 *
 * Opcode      Instruction        Clocks     Description
 * C8 iw 00    ENTER imm16,0      10         Make procedure stack frame
 * C8 iw 01    ENTER imm16,1      12         Make stack frame for procedure parameters
 * C8 iw ib    ENTER imm16,imm8   15+4(n-1)  Make stack frame for procedure parameters
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see LEAVE
 */
public class ENTER extends AbstractDualOperandOpCode {

    /**
     * ENTER dest, src
     *
     * @param dest the destination {@link Operand operand}
     * @param src  the source {@link Operand operand}
     */
    public ENTER(final Operand dest, final Operand src) {
        super("ENTER", dest, src);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        // for 386+
        // TODO re-examine this logic
        if (cpu.isVirtualMode()) {
            cpu.getStack().push(cpu.EBP);
            cpu.EBP.set(cpu.ESP.get());
        }

        // 286 or older ...
        else {
            cpu.getStack().push(cpu.BP);
            cpu.BP.set(cpu.SP.get());
        }
    }

}
