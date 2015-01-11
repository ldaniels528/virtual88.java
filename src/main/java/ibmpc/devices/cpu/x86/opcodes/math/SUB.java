package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.system.IbmPcSystem;

/**
 * SUB dest, src
 *
 * @author lawrence.daniels@gmail.com
 */
public class SUB extends AbstractDualOperandOpCode {

    /**
     * SUB dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public SUB(final Operand dest, final Operand src) {
        super("SUB", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        dest.set(cpu.FLAGS.updateSUB(dest, src));
    }

}
