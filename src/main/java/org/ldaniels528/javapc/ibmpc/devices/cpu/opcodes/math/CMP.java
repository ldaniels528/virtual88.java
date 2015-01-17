package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Compare (CMP)
 * <pre>
 * Usage:  CMP     dest,src
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Subtracts source from destination and updates the flags but does
 * not save result.  Flags can subsequently be checked for conditions.
 *
 *                               Clocks                 Size
 *      Operands         808x  286   386   486          Bytes
 *
 *      reg,reg           3     2     2     1             2
 *      mem,reg          9+EA   7     5     2            2-4  (W88=13+EA)
 *      reg,mem          9+EA   6     6     2            2-4  (W88=13+EA)
 *      reg,immed         4     3     2     1            3-4
 *      mem,immed       10+EA   6     5     2            3-6  (W88=14+EA)
 *      accum,immed       4     3     2     1            2-3
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
@FlagsAffected({"AF", "CF", "OF", "PF", "SF", "ZF"})
public class CMP extends AbstractDualOperandOpCode {

    /**
     * CMP dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public CMP(final Operand dest, final Operand src) {
        super("CMP", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.updateSUB(dest, src);
    }

}

