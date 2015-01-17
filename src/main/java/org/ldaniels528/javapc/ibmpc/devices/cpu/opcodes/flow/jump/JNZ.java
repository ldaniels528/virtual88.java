package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractFlowControlOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * <pre>
 * Jump if Not Zero (JNZ) / Jump if Not Equal (JNE)
 * Jump Condition: ZF=0
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JNZ extends AbstractFlowControlOpCode {

    /**
     * Creates a new conditional jump instruction
     *
     * @param destination the given memory offset to jump to.
     */
    public JNZ(final Operand destination) {
        super(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean redirectsFlow(final I8086 cpu) {
        return !cpu.FLAGS.isZF();
    }

}