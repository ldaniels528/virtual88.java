package org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;

/**
 * Represents a pointer to a 32-bit (double word) memory address
 *
 * @author lawrence.daniels@gmail.com
 */
public class DWordPtr implements MemoryPointer {
    private final MemoryReference memoryRef;
    private final I8086 cpu;

    /**
     * Creates a pointer to a 32-bit memory address
     *
     * @param memoryRef the given {@link MemoryReference memory reference}
     */
    public DWordPtr(final MemoryReference memoryRef) {
        this.cpu = memoryRef.getCPU();
        this.memoryRef = memoryRef;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int get() {
        return cpu.getDoubleWord(memoryRef.getOffset());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final int value) {
        // get the offset of the memory reference
        final int offset = memoryRef.getOffset();

        // set the value
        cpu.setDoubleWord(offset, value);
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
        return SIZE_32BIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("DWORD PTR %s", memoryRef);
    }

}
