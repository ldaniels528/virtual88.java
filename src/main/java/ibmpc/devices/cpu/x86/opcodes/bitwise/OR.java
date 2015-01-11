package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.system.IbmPcSystem;

/**
 * OR dest,src
 *
 * @author lawrence.daniels@gmail.com
 */
public class OR extends AbstractDualOperandOpCode {

    /**
     * OR dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public OR(final Operand dest, final Operand src) {
        super("OR", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        dest.set(cpu.FLAGS.updateOR(dest, src));
    }

}