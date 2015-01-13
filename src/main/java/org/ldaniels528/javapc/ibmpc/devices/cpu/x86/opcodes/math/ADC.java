package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Add with Carry (ADC)
 * <pre>
 * Usage:  ADC     dest,src
 * Modifies flags: AF CF OF SF PF ZF
 *
 * Sums two binary operands placing the result in the destination.
 * If CF is set, a 1 is added to the destination.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ADC extends AbstractDualOperandOpCode {

    /**
     * ADC dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public ADC(final Operand dest, final Operand src) {
        super("ADC", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        dest.set((cpu.FLAGS.isCF() ? 1 : 0) + cpu.FLAGS.updateADD(dest, src));
    }

}
