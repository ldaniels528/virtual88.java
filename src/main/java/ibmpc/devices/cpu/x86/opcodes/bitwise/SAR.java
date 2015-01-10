/**
 *
 */
package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * <pre>
 * Shift Arithmetic Right
 *
 * 	Usage:  SAR     dest,count
 * 	        SHR     dest,count
 * 	Modifies flags: CF OF PF SF ZF (AF undefined)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see SHR
 */
public class SAR extends AbstractDualOperandOpCode {

    /**
     * SAR dst, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public SAR(final Operand dest, final Operand src) {
        super("SAR", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        // get the source and destination values
        final int value0 = dest.get();
        final int value1 = src.get();

        // shift the destination right by the source value
        dest.set(value0 >> value1);

        // put the right most bit into CF
        cpu.FLAGS.setCF((value0 & 0x01) != 0);
    }

}