package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Syntax: ADD dest, src
 * dest: register or memory
 * src: register, memory, or immediate
 * Action: dest = dest + src
 *
 * Flags Affected: OF, SF, ZF, AF, PF, CF
 * Notes: Works for both signed and unsigned numbers.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ADD extends AbstractDualOperandOpCode {

    /**
     * ADD dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public ADD(final Operand dest, final Operand src) {
        super("ADD", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        dest.set(cpu.FLAGS.updateADD(dest, src));
    }

}
