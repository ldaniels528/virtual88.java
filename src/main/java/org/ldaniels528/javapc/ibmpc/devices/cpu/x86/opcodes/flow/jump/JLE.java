package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Lower or Equal
 * Jump Condition: ZF=1 or SF != OF
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JLE extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param offset the given memory offset to jump to.
     */
    public JLE(final Operand offset) {
        super(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        return cpu.FLAGS.isZF() || (cpu.FLAGS.isSF() != cpu.FLAGS.isOF());
    }

}