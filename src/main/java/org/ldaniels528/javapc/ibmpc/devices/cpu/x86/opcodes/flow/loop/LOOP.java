package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.loop;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * LOOP destination
 *
 * @author lawrence.daniels@gmail.com
 */
public class LOOP extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional loop instruction
     *
     * @param destination the given memory offset to jump to.
     */
    public LOOP(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        final boolean ok = cpu.CX.get() > 0;
        if (ok) {
            // decrement cx
            cpu.CX.add(-1);
        }
        return ok;
    }

}