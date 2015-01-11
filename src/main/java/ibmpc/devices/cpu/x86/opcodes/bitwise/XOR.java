package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  XOR     dest,src
 * Modifies flags: CF OF PF SF ZF (AF undefined)
 *
 * 	Performs a bitwise exclusive OR of the operands and returns
 * 	the result in the destination.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class XOR extends AbstractDualOperandOpCode {

    /**
     * XOR dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public XOR(final Operand dest, final Operand src) {
        super("XOR", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        dest.set(cpu.FLAGS.updateXOR(dest, src));
    }

}