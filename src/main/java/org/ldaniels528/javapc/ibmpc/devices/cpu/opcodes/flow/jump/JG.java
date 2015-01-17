package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractFlowControlOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * <pre>
 * Jump if Greater
 * Jump Condition: ZF=0 and SF=OF
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JG extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param offset the given memory offset to jump to.
     */
    public JG(final Operand offset) {
        super(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        return !cpu.FLAGS.isZF() && (cpu.FLAGS.isSF() == cpu.FLAGS.isOF());
    }

}