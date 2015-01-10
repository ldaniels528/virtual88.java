package ibmpc.devices.cpu.x86.opcodes.data;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * <pre>
 * Exchange
 *
 * Usage:  XCHG    dest,src
 * Modifies flags: None
 *
 *  Exchanges contents of source and destination.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class XCHG extends AbstractDualOperandOpCode {

    /**
     * XCHG dest, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public XCHG(final Operand dest, final Operand src) {
        super("XCHG", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        // save the destination's current value
        final int destValue = dest.get();

        // exchange the source and destination values
        dest.set(src.get());
        src.set(destValue);
    }

}
