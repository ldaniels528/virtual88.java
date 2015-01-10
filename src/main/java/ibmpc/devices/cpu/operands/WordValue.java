package ibmpc.devices.cpu.operands;

/**
 * X86 16-bit Operand Value
 *
 * @author lawrence.daniels@gmail.com
 */
public class WordValue implements OperandValue {
    private final int value;

    /**
     * Creates a new 16-bit operand value
     *
     * @param value the given value
     */
    public WordValue(final int value) {
        this.value = (value & 0xFFFF);
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.opcode.X86Operand#get()
     */
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
        return SIZE_16BIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%04X", value);
    }

}
