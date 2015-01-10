package ibmpc.devices.cpu.x86.opcodes.flow.jump;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Above
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
    protected boolean redirectsFlow(final Intel80x86 cpu) {
        return (!cpu.FLAGS.isCF() && !cpu.FLAGS.isZF());
    }

}
