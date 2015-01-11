package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.loop;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * LOOPZ destination
 *
 * @author lawrence.daniels@gmail.com
 */
public class LOOPZ extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional loop instruction
     *
     * @param destination the given memory offset to jump to.
     */
    public LOOPZ(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final Intel80x86 cpu) {
        final boolean ok = (cpu.CX.get() > 0) && (cpu.FLAGS.isZF());
        if (ok) {
            // decrement cx
            cpu.CX.add(-1);
        }
        return ok;
    }

}