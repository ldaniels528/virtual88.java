package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Below or Equal (JBE) / Jump if Not Above (JNA)
 * Jump Condition: CF=1 or ZF=1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JBE extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param offset the given memory offset to jump to.
     */
    public JBE(final Operand offset) {
        super(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final Intel8086 cpu) {
        return cpu.FLAGS.isCF() || cpu.FLAGS.isZF();
    }

}