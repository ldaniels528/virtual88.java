package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * <pre>
 * Syntax: ADD dest, src
 * dest: register or memory
 * src: register, memory, or immediate
 * Action: dest = dest + src
 *
 * Flags Affected: OF, SF, ZF, AF, PF, CF
 * Notes: Works for both signed and unsigned numbers.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ADD extends AbstractDualOperandOpCode {

    /**
     * ADD dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public ADD(final Operand dest, final Operand src) {
        super("ADD", dest, src);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        // cache the values (registers are slower)
        final int value0 = dest.get();
        final int value1 = src.get();

        // calculate the sum of the values
        final int sum0 = value0 + value1;
        final int sum1 = value0 | value1;

        // update the flags
        final X86ExtendedFlags flags = cpu.FLAGS;
        flags.setCF(sum0 != sum1);
        flags.setOF(sum0 > 0xFFFF);
        flags.setZF(sum0 == 0);
        flags.setSF(sum0 > 0x7FFF);
        flags.setPF(sum0 % 2 == 0);
        //flags.setAF( value == 0 );

        // set the dest
        dest.set(sum0);
    }

}
