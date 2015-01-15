package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <h4>Bitwise logical OR</h4>
 * Syntax:	OR	dest, src
 * dest: register or memory
 * src: register, memory, or immediate
 * Action: dest = dest | src
  * Flags Affected: OF=0, SF, ZF, AF=?, PF, CF=0
 * @author lawrence.daniels@gmail.com
 */
@FlagsAffected({"AF", "CF", "OF", "PF", "SF", "ZF"})
public class OR extends AbstractDualOperandOpCode {

    /**
     * OR dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public OR(final Operand dest, final Operand src) {
        super("OR", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        dest.set(cpu.FLAGS.updateOR(dest, src));
    }

}