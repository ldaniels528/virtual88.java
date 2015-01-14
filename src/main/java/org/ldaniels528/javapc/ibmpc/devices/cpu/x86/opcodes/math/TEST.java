package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * TEST - Test For Bit Pattern
 *
 * 	Usage:  TEST    dest,src
 * 	Modifies flags: CF OF PF SF ZF (AF undefined)
 *
 * 	Performs a logical AND of the two operands updating the flags
 * 	register without saving the result.
 *
 * 	                         Clocks                 Size
 * 	Operands         808x  286   386   486          Bytes
 *
 * 	reg,reg           3     2     1     1             2
 * 	reg,mem          9+EA   6     5     1            2-4  (W88=13+EA)
 * 	mem,reg          9+EA   6     5     2            2-4  (W88=13+EA)
 * 	reg,immed         5     3     2     1            3-4
 * 	mem,immed       11+EA   6     5     2            3-6
 * 	accum,immed       4     3     2     1            2-3
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class TEST extends AbstractDualOperandOpCode {

    /**
     * TEST dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public TEST(final Operand dest, final Operand src) {
        super("TEST", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.updateAND(src, dest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s %s,%s", getClass().getSimpleName(), dest, src);
    }

}
