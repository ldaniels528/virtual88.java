package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * <pre>
 * Roll Carry Left (RCL)
 *
 * Rotate operand1 left through Carry Flag. The number of rotates
 * is set by operand2.
 *
 * When immediate is greater then 1, assembler generates several
 * RCL xx, 1 instructions because 8086 has machine code only for
 * this instruction (the same principle works for all other
 * shift/rotate instructions).
 *
 * Algorithm:
 *
 * shift all bits left, the bit that goes off is set to CF and
 * previous value of CF is inserted to the right-most position.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class RCL extends AbstractDualOperandOpCode {

    /**
     * RCL dst, src (e.g. 'RCL AL, 1')
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public RCL(final Operand dest, final Operand src) {
        super("RCL", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        // get the destination value
        final int value = dest.get();

        // get the count of bits to roll
        final int count = src.get();

        // shift left the value by "count" bits
        // to get the rolled portion
        final int shl = (value << count);

        // get the carry bit
        final int carry = cpu.FLAGS.isCF() ? 1 : 0;

        // put the value into the destination, CF = 0
        dest.set(shl | carry);
        cpu.FLAGS.setCF(false);
    }

}