package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers;

/**
 * Represents a n-bit register
 *
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86GenericRegister implements X86Register {
    protected final String name;
    protected final int mask;
    private final int index;
    protected int value;

    /**
     * Creates an instance of this register
     *
     * @param name  the name of this register
     * @param mask  the mask that will determine the size of this register
     * @param index the given index of this register
     */
    public X86GenericRegister(final String name, final int mask, final int index) {
        this.name = name;
        this.mask = mask;
        this.index = index;
        this.value = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int get() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int value) {
        this.value = (value & mask);
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
    public void setBit(int bitNum, boolean on) {
        // turn the bit on?
        if (on) {
            final int mask = bitNum << 1;
            value |= mask;
        }
        // turn the bit off...
        else {
            final int mask = 0xFFFF - (bitNum << 1);
            value &= mask;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int add(int delta) {
        this.value = (value + delta) & mask;
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void and(int mask) {
        this.value &= mask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void or(int mask) {
        this.value |= mask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lshift(int bits) {
        this.value <<= bits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rshift(int bits) {
        this.value >>= bits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }

}
