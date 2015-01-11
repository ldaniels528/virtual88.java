package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Roll Right (ROR)
 * <pre>
 * 		Usage:  ROR     dest,count
 *      Modifies flags: CF OF
 *
 *         +-----------------------+        +---+
 *     +-->| 7 ----------------> 0 |---+--->| C |
 *     |   +-----------------------+   |    +---+
 *     |                               |
 *     + ------------------------------+
 *
 *      Rotates the bits in the destination to the right "count" times with
 *      all data pushed out the left side re-entering on the left.  The
 *      Carry Flag will contain the value of the last bit rotated out.
 *
 *                                Clocks                 Size
 *      Operands         808x  286   386   486          Bytes
 *
 *      reg,1             2     2     3     3             2
 *      mem,1           15+EA   7     7     4            2-4  (W88=23+EA)
 *      reg,CL           8+4n  5+n    3     3             2
 *      mem,CL        20+EA+4n 8+n    7     4            2-4  (W88=28+EA+4n)
 *      reg,immed8        -    5+n    3     2             3
 *      mem,immed8        -    8+n    7     4            3-5
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ROR extends AbstractDualOperandOpCode {

    /**
     * ROR dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand count}
     */
    public ROR(final Operand dest, final Operand src) {
        super("ROR", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
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