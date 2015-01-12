package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Usage:  JCXZ    label
 * Modifies flags: None
 *
 * Causes execution to branch to "label" if register CX is zero.
 * Uses unsigned comparison.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JCXZ extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param destination the given memory offset to jump to.
     */
    public JCXZ(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final Intel8086 cpu) {
        return cpu.CX.get() == 0;
    }

}
