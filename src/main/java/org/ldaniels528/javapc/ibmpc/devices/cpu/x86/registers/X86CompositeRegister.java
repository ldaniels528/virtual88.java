package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers;

import static org.ldaniels528.javapc.util.BitMaskGenerator.turnBitOffMask;
import static org.ldaniels528.javapc.util.BitMaskGenerator.turnBitOnMask;

/**
 * Represents a composite register; a register composed of
 * two parts, high and low respectively. (e.g. AX = AH:AL)
 *
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86CompositeRegister implements X86Register {
    private final X86Register hi;
    private final X86Register lo;
    private final String name;
    private final int shift;
    private final int index;

    /**
     * Default Constructor
     *
     * @param name  the name of this register
     * @param hi    the high portion of this register
     * @param lo    the low portion of this register
     * @param shift the number of bits to shift in compositing the values
     * @param index the given register index
     */
    public X86CompositeRegister(String name, X86Register hi, X86Register lo, int shift, int index) {
        this.name = name;
        this.hi = hi;
        this.lo = lo;
        this.shift = shift;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int get() {
        return (hi.get() << shift) | lo.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final int value) {
        hi.set((value & 0xff00) >> shift);
        lo.set(value & 0x00ff);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int add(final int delta) {
        final int value0 = get() + delta;
        set(value0);
        return value0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void and(final int mask) {
        set(get() & mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void or(final int mask) {
        set(get() | mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lshift(final int bits) {
        set(get() << bits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rshift(final int bits) {
        set(get() >> bits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBit(final int bitNum, final boolean on) {
        // get the current value
        final int value0 = get();

        // turn on/off the bit
        if (on) set(value0 | turnBitOnMask(bitNum));
        else set(value0 & turnBitOffMask(2 * shift, bitNum));
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

}
