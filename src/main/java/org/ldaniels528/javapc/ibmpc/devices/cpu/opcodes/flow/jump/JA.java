package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractFlowControlOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * <pre>
 * Jump if Above (JA) / Jump if Not Below or Equal (JNBE)
 * Jump Condition: CF=0 and ZF=0
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JA extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param destination the given memory offset to jump to.
     */
    public JA(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        return !cpu.FLAGS.isCF() && !cpu.FLAGS.isZF();
    }

}
