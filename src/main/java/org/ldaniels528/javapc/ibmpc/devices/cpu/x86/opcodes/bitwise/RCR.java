package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Roll Carry Right (RCR)
 *
 * Rotate operand1 right through Carry Flag. The number
 * of rotates is set by operand2.
 *
 * Algorithm:
 *
 * shift all bits right, the bit that goes off is set to CF and previous
 * value of CF is inserted to the left-most position.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class RCR extends AbstractDualOperandOpCode {

    /**
     * RCR dst, src (e.g. 'RCR AL, 1')
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public RCR(final Operand dest, final Operand src) {
        super("RCR", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // get the destination value
        final int value = dest.get();

        // get the count of bits to roll
        final int count = src.get();

        // get the size of the destination in bits
        final int size = dest.size();

        // shift right the value by "count" bits
        // to get the rolled portion
        final int shr = (value >> count);

        // shift left the value by "size"-"count" bits
        // to get the rolled off portion
        final int shl = (value << (size - count));

        // determine the last bit rolled
        final int mask = computeMask(count);
        final boolean bit = ((value & mask) >= 1);

        // put the value into the destination
        // put the last bit rolled into CF
        dest.set(shl | shr);
        cpu.FLAGS.setCF(bit);
    }

    /**
     * Computes the mask for determining the list bit rolled
     *
     * @param count the given number of bits to roll
     * @return the mask for determining the list bit rolled
     */
    private int computeMask(final int count) {
        return (1 << (count - 1));
    }

}