package org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;

/**
 * Represents a pointer to an 8-bit (byte) memory address
 *
 * @author lawrence.daniels@gmail.com
 */
public class BytePtr implements MemoryPointer {
    private final MemoryReference memoryRef;
    private final Intel8086 cpu;

    /**
     * Creates a pointer to a 8-bit memory address
     *
     * @param memoryRef the given {@link MemoryReference memory reference}
     */
    public BytePtr(final MemoryReference memoryRef) {
        this.memoryRef = memoryRef;
        this.cpu = memoryRef.getCPU();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int get() {
        return cpu.getByte(memoryRef.getOffset());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final int value) {
        // get the offset of the memory reference
        final int offset = memoryRef.getOffset();

        // set the value
        cpu.setByte(offset, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemoryReference getMemoryReference() {
        return memoryRef;
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
        return String.format("BYTE PTR %s", memoryRef);
    }

}
