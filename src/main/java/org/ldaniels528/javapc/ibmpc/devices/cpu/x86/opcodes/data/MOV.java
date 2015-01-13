package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  MOV     dest,src
 * Modifies flags: None
 *
 * Copies byte or word from the source operand to the destination
 * operand.  If the destination is SS interrupts are disabled except
 * on early buggy 808x CPUs.  Some CPUs disable interrupts if the
 * destination is any of the segment registers
 *
 *                           Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *
 * reg,reg           2     2     2     1             2
 * mem,reg          9+EA   3     2     1            2-4  (W88=13+EA)
 * reg,mem          8+EA   5     4     1            2-4  (W88=12+EA)
 * mem,immed       10+EA   3     2     1            3-6  (W88=14+EA)
 * reg,immed         4     2     2     1            2-3
 * mem,accum         10    3     2     1             3   (W88=14)
 * accum,mem         10    5     4     1             3   (W88=14)
 * segreg,reg16      2     2     2     3             2
 * segreg,mem16     8+EA   5     5     9            2-4  (W88=12+EA)
 * reg16,segreg      2     2     2     3             2
 * mem16,segreg     9+EA   3     2     3            2-4  (W88=13+EA)
 * reg32,CR0/CR2/CR3 -     -     6     4
 * CR0,reg32         -     -     10    16
 * CR2,reg32         -     -     4     4             3
 * CR3,reg32         -     -     5     4             3
 * reg32,DR0/DR1/DR2/DR3   -     22   10             3
 * reg32,DR6/DR7     -     -     22   10             3
 * DR0/DR1/DR2/DR3,reg32   -     22   11             3
 * DR6/DR7,reg32     -     -     16   11             3
 * reg32,TR6/TR7     -     -     12    4             3
 * TR6/TR7,reg32     -     -     12    4             3
 * reg32,TR3                           3
 * TR3,reg32                           6
 *
 *       - when the 386 special registers are used all operands are 32 bits
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class MOV extends AbstractDualOperandOpCode {

    /**
     * MOV dst, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public MOV(final Operand dest, final Operand src) {
        super("MOV", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) {
        dest.set(src.get());
    }

}
