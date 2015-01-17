package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Roll Left (ROL)
 * <pre>
 * 		Usage:  ROL     dest,count
 *      Modifies flags: CF OF
 *
 * +---+       +-----------------------+
 * | C |<---+--| 7 <---------------- 0 |<--+
 * +---+    |  +-----------------------+   |
 *          |                              |
 *          +------------------------------+
 *
 *      Rotates the bits in the destination to the left "count" times with
 *      all data pushed out the left side re-entering on the right.  The
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
@FlagsAffected({"CF", "OF"})
public class ROL extends AbstractDualOperandOpCode {

    /**
     * ROL dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public ROL(final Operand dest, final Operand src) {
        super("ROL", dest, src);
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

        // shift left the value by "count" bits
        // to get the rolled portion
        final int shl = (value << count);

        // shift right the value by "size"-"count" bits
        // to get the rolled off portion
        final int shr = (value >> (size - count));

        // determine the last bit rolled
        final int mask = computeMask(size, count);
        final boolean bit = ((value & mask) >= 1);

        // put the value into the destination
        // put the last bit rolled into CF
        dest.set(shl | shr);
        cpu.FLAGS.setCF(bit);
        cpu.FLAGS.setOF(value < (shl | shr));
    }

    /**
     * Computes the mask for determining the list bit rolled
     *
     * @param size  the size in bits of the operand
     * @param count the given number of bits to roll
     * @return the mask for determining the list bit rolled
     */
    private int computeMask(final int size, final int count) {
        return (1 << (size - count));
    }

}