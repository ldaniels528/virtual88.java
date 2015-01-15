package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Subtract with Borrow (SBB)
 * <pre>
 * Usage: SBB dest, src
 *
 * Algorithm:
 * operand1 = operand1 - operand2 - CF
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
@FlagsAffected({"AF", "CF", "OF", "PF", "SF", "ZF"})
public class SBB extends AbstractDualOperandOpCode {

    /**
     * SBB dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public SBB(final Operand dest, final Operand src) {
        super("SBB", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        final int borrow = cpu.FLAGS.isCF() ? 1 : 0;
        dest.set(cpu.FLAGS.updateADD(dest, src) - borrow);
    }

}
