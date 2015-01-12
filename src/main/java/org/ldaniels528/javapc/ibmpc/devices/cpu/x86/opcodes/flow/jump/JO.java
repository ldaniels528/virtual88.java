package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Overflow
 * Jump Condition: OF=1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JO extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param destination the given memory offset to jump to.
     */
    public JO(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final Intel8086 cpu) {
        return cpu.FLAGS.isOF();
    }

}