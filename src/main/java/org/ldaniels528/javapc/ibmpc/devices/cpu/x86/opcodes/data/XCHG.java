package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

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
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        // save the destination's current value
        final int destValue = dest.get();

        // exchange the source and destination values
        dest.set(src.get());
        src.set(destValue);
    }

}
