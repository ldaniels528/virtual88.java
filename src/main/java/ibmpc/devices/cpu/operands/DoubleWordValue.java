package ibmpc.devices.cpu.operands;

/**
 * X86 32-bit Operand Value
 *
 * @author lawrence.daniels@gmail.com
 */
public class DoubleWordValue implements OperandValue {
    private final int value;

    /**
     * Creates a new 32-bit operand value
     *
     * @param value the given value
     */
    public DoubleWordValue(final int value) {
        this.value = value;
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
        return SIZE_32BIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%08X", value);
    }

}
