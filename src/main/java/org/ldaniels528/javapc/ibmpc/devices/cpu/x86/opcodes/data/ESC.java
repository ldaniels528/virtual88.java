package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.ByteValue;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Escape
 *
 * Usage:  ESC     immed,src
 * Modifies flags: None
 *
 * Provides access to the data bus for other resident processors.
 * The CPU treats it as a NOP but places memory operand on bus.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ESC extends AbstractOpCode {
    public static final ByteValue ESC_09 = new ByteValue(0x09);
    public static final ByteValue ESC_19 = new ByteValue(0x19);
    public static final ByteValue ESC_1C = new ByteValue(0x1C);
    public static final ByteValue ESC_1E = new ByteValue(0x1E);
    public static final ByteValue ESC_29 = new ByteValue(0x29);
    public static final ByteValue ESC_2D = new ByteValue(0x2D);
    public static final ByteValue ESC_39 = new ByteValue(0x29);
    private final Operand immed;
    private final Operand src;

    /**
     * ESC immed, src
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    public ESC(final Operand dest, final Operand src) {
        this.immed = dest;
        this.src = src;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        // TODO figure this out
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("ESC %s,%s", immed, src);
    }

}
