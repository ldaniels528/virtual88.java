/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.*;

/**
 * <pre>
 * Shift Arithmetic Left
 *
 * 	Usage:  SAL     dest,count
 * 	        SHL     dest,count
 * 	Modifies flags: CF OF PF SF ZF (AF undefined)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see SHL
 */
public class SAL extends AbstractDualOperandOpCode {

    /**
     * SAL dst, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public SAL(final Operand dest, final Operand src) {
        super("SAL", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        // get the source and destination values
        final int value0 = dest.get();
        final int value1 = src.get();

        // shift the destination left by the source value
        dest.set(value0 << value1);

        // put the left most bit into CF
        cpu.FLAGS.setCF((value0 & getBitMask(dest)) != 0);
    }

    /**
     * Returns the appropriate mask for determine the left most bit
     *
     * @param operand the given value
     * @return the mask for determine the left most bit
     */
    private int getBitMask(final Operand operand) {
        switch (operand.size()) {
            case SIZE_8BIT:
                return 0x80;        // 1000 0000
            case SIZE_16BIT:
                return 0x8000;        // 1000 0000 0000 0000
            default:
                throw new IllegalArgumentException("Unhandled operand size");
        }
    }

}