package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;

/**
 * 80x86 Decode Processor
 *
 * @author lawrence.daniels@gmail.com
 */
public interface DecodeProcessor {

    /**
     * Decodes the next instruction
     *
     * @return the next {@link OpCode opCode}
     */
    OpCode decodeNext();

    /**
     * Initializes the decoder before decoding starts
     */
    void init();

    /**
     * Shutsdown the decoder after processing has completed
     */
    void shutdown();

    /**
     * Clears the queue as a result of a branch change,
     * and pointers the decoder to the new segment and offset.
     *
     * @param segment the new decode segment
     * @param offset  the new decode offset
     */
    void redirect(final int segment, final int offset);

}
