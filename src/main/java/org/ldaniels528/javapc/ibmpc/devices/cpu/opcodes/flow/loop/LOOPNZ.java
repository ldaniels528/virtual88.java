package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.loop;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractFlowControlOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * LOOP, LOOPE, LOOPZ, LOOPNE, LOOPNZ: Loop with Counter
 * <p/>
 * LOOP imm                      ; E2 rb                [8086]
 * LOOP imm,CX                   ; a16 E2 rb            [8086]
 * LOOP imm,ECX                  ; a32 E2 rb            [386]
 * <p/>
 * LOOPE imm                     ; E1 rb                [8086]
 * LOOPE imm,CX                  ; a16 E1 rb            [8086]
 * LOOPE imm,ECX                 ; a32 E1 rb            [386]
 * LOOPZ imm                     ; E1 rb                [8086]
 * LOOPZ imm,CX                  ; a16 E1 rb            [8086]
 * LOOPZ imm,ECX                 ; a32 E1 rb            [386]
 * <p/>
 * LOOPNE imm                    ; E0 rb                [8086]
 * LOOPNE imm,CX                 ; a16 E0 rb            [8086]
 * LOOPNE imm,ECX                ; a32 E0 rb            [386]
 * LOOPNZ imm                    ; E0 rb                [8086]
 * LOOPNZ imm,CX                 ; a16 E0 rb            [8086]
 * LOOPNZ imm,ECX                ; a32 E0 rb            [386]
 * <p/>
 * LOOP decrements its counter register (either CX or ECX--if one is not specified explicitly,
 * the BITS setting dictates which is used) by one, and if the counter does not become zero as
 * a result of this operation, it jumps to the given label. The jump has a range of 128 bytes.
 * <p/>
 * LOOPE (or its synonym LOOPZ) adds the additional condition that it only jumps if the counter
 * is nonzero and the zero flag is set. Similarly, LOOPNE (and LOOPNZ) jumps only if the counter
 * is nonzero and the zero flag is clear.
 *
 * @author lawrence.daniels@gmail.com
 */
public class LOOPNZ extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional loop instruction
     *
     * @param destination the given offset code.
     */
    public LOOPNZ(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        final boolean ok = (cpu.CX.get() > 0) && !cpu.FLAGS.isZF();
        if (ok) {
            // decrement cx
            cpu.CX.add(-1);
        }
        return ok;
    }

}