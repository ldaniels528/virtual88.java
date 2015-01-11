package org.ldaniels528.javapc.ibmpc.devices.cpu.operands;

/**
 * Represents an 8-bit unsigned operand value
 *
 * @author lawrence.daniels@gmail.com
 */
public class ByteValue implements OperandValue {
    public static final ByteValue ONE = new ByteValue(1);
    private final int value;

    /**
     * Creates a new unsigned byte (8-bit) operand value
     *
     * @param value the given value
     */
    public ByteValue(final int value) {
        this.value = value & 0x00FF;
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
    public void set(final int value) {
        throw new IllegalStateException("Cannot be set");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return SIZE_8BIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%02X", value);
    }

}
