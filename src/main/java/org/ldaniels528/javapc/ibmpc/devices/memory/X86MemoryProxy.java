package org.ldaniels528.javapc.ibmpc.devices.memory;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.apache.log4j.Logger;

import static java.lang.String.format;

/**
 * Acts as a proxy to the random access memory (RAM) instance
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86MemoryProxy {
    private final Logger logger = Logger.getLogger(getClass());
    private final IbmPcRandomAccessMemory memory;
    private int segment;
    private int offset;

    /**
     * Creates a new memory proxy instance
     *
     * @param memory  the given {@link IbmPcRandomAccessMemory random access memory} instance
     * @param segment the given memory segment
     * @param offset  the given memory offset
     */
    public X86MemoryProxy(final IbmPcRandomAccessMemory memory,
                          final int segment,
                          final int offset) {
        this.memory = memory;
        this.segment = segment;
        this.offset = offset;
    }

    /**
     * @return the random access memory (RAM) instance
     */
    public IbmPcRandomAccessMemory getMemory() {
        return memory;
    }

    /**
     * Points this proxy to the given destination
     *
     * @param destination the given {@link Operand destination}
     */
    public void setDestination(final Operand destination) {
        offset = destination.get();
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the memory offset
     *
     * @param offset the offset to set
     */
    public void setOffset(final int offset) {
        this.offset = offset;
    }

    /**
     * Moves the offset pointer by the given delta value
     *
     * @param delta the given (+/-) delta value
     */
    public void moveOffset(final int delta) {
        offset += delta;
    }

    /**
     * @return the segment
     */
    public int getSegment() {
        return segment;
    }

    /**
     * Sets the memory segment
     *
     * @param segment the segment to set
     */
    public void setSegment(int segment) {
        this.segment = segment;
    }

    /**
     * Returns the next byte from memory without advancing the pointer
     *
     * @return the next byte
     */
    public int peekAtNextByte() {
        return memory.getByte(segment, offset);
    }

    /**
     * Returns the next word from memory without advancing the pointer
     *
     * @return the next word
     */
    public int peekAtNextWord() {
        return memory.getWord(segment, offset);
    }

    /**
     * Returns the next byte from memory advancing the
     * pointer by 1 byte.
     *
     * @return the next byte
     */
    public int nextByte() {
        return memory.getByte(segment, offset++);
    }

    /**
     * Returns the next specified number of bytes from memory advancing the
     * pointer by the number of bytes retrieved.
     *
     * @return the memory block
     */
    public byte[] nextBytes(final int count) {
        final byte[] bytes = memory.getBytes(segment, offset, count);
        offset += bytes.length;
        return bytes;
    }

    /**
     * Returns the next word (little endian) from memory advancing the
     * pointer by 2 bytes.
     *
     * @return the next word
     */
    public int nextWord() {
        final int word = memory.getWord(segment, offset);
        offset += 2;
        return word;
    }

    /**
     * Reads the next byte from the proxy and combines it
     * with the given byte (low order byte) resulting in a
     * word.
     *
     * @param lowByte the given low order byte
     * @return the resultant word
     */
    public int nextWord(final int lowByte) {
        return (nextByte() << 8) | lowByte;
    }

    /**
     * Sets the given byte in the next memory offset
     *
     * @param byteCode the given byte code
     */
    public void setByte(final int offset, final int byteCode) {
        memory.setByte(segment, offset, (byte) byteCode);
    }

    /**
     * Sets the given byte in the next memory offset
     *
     * @param byteCode the given byte code
     */
    public void setByte(final int byteCode) {
        memory.setByte(segment, offset++, (byte) byteCode);
        logger.debug(format("byte %02X", byteCode));
    }

    /**
     * Sets the given bytes starting at the next memory offset
     *
     * @param opCodes the given byte codes
     */
    public void setBytes(final int... opCodes) {
        // create the byte code from the opCodes
        final byte[] bytecodes = new byte[opCodes.length];
        for (int n = 0; n < bytecodes.length; n++) {
            bytecodes[n] = (byte) opCodes[n];
        }

        // write the bytes to memory
        setBytes(bytecodes);
    }

    /**
     * Sets the given bytes starting at the next memory offset
     *
     * @param bytecodes the given byte codes
     */
    public void setBytes(final byte... bytecodes) {
        memory.setBytes(segment, offset, bytecodes, bytecodes.length);
        offset += bytecodes.length;
    }

    /**
     * Sets the given word in the next memory offset
     *
     * @param offset   the given offset in memory
     * @param wordcode the given word code (2 bytes)
     */
    public void setWord(final int offset, final int wordcode) {
        memory.setWord(segment, offset, wordcode);
    }

    /**
     * Sets the given word in the next memory offset
     *
     * @param wordcode the given word code (2 bytes)
     */
    public void setWord(final int wordcode) {
        memory.setWord(segment, offset, wordcode);
        offset += 2;
    }

}
