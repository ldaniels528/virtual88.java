package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * Subtract with Borrow (SBB)
 * <pre>
 * Usage: SBB dest, src
 *
 * Algorithm:
 * operand1 = operand1 - operand2 - CF
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class SBB extends AbstractDualOperandOpCode {

    /**
     * SBB dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public SBB(final Operand dest, final Operand src) {
        super("SBB", dest, src);
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu) {
        // cache the flags
        final X86ExtendedFlags flags = cpu.FLAGS;

        // cache the values (registers are slower)
        final int value0 = dest.get();
        final int value1 = src.get();

        // calculate the sum of the values
        final int diff0 = (value0 - value1) - (flags.isCF() ? 1 : 0);

        // update the flags
        flags.setOF(diff0 > 0xFFFF);
        flags.setZF(diff0 == 0);
        flags.setSF(diff0 > 0x7FFF);
        flags.setPF(diff0 % 2 == 0);
        //flags.setAF( value == 0 );

        // set the dest
        dest.set(diff0);
    }

}
