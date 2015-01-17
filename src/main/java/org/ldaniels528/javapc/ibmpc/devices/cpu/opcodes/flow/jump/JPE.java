package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Parity is Even
 * Jump Condition: PF=1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JPE extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param offset the given memory offset to jump to.
     */
    public JPE(final Operand offset) {
        super(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        return cpu.FLAGS.isPF();
    }

}