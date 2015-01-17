package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractForcedRedirectOpCode;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * 8086 Decode Processor
 *
 * @author lawrence.daniels@gmail.com
 */
public class DecodeProcessorImpl implements DecodeProcessor {
    private final IbmPcRandomAccessMemory memory;
    private final X86MemoryProxy proxy;
    private final Decoder[] decoders;
    private final I8086 cpu;

    /**
     * Creates a new instance decode processor
     *
     * @param cpu   the given {@link I8086 CPU}
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     */
    public DecodeProcessorImpl(final I8086 cpu, final X86MemoryProxy proxy) {
        this.cpu = cpu;
        this.proxy = proxy;
        this.memory = proxy.getMemory();
        this.decoders = new Decoder[]{
                new Decoder00(), new Decoder10(), new Decoder20(), new Decoder30(),
                new Decoder40(), new Decoder50(), new Decoder60(), new Decoder70(),
                new Decoder80(), new Decoder90(), new DecoderA0(), new DecoderB0(),
                new DecoderC0(), new DecoderD0(), new DecoderE0(), new DecoderF0(this)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decodeNext() {
        // capture the current offset
        final int offset0 = proxy.getOffset();

        // peek at the next byte
        final int code = proxy.peekAtNextByte();

        // get the first hex digit (mask = 1111 0000)
        final int index = ((code & 0xF0) >> 4);

        // invoke the appropriate interpreter
        final OpCode opCode = decoders[index].decode(cpu, proxy, this);

        // get the length of the instruction
        final int codeLength = proxy.getOffset() - offset0;

        // capture the instruction's machine code
        final long insCode = getInstructionCode(offset0, codeLength);

        // set the instruction code and length
        opCode.setLength(codeLength);
        opCode.setInstructionCode(insCode);

        // is it a forced redirect?
        if (opCode.isForcedRedirect()) {
            final AbstractForcedRedirectOpCode forcedRedirectOpCode = (AbstractForcedRedirectOpCode) opCode;
            final Operand destination = forcedRedirectOpCode.getDestination();
            proxy.setDestination(destination);
        }

        // return the opCode
        return (opCode.isConditional())
                ? new FlowControlCallBackOpCode(this, opCode)
                : opCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void redirect(final int segment, final int offset) {
        proxy.setSegment(segment);
        proxy.setOffset(offset);
    }

    /**
     * Retrieves the instruction code
     *
     * @param offset the initial offset of the instruction
     * @return the instruction code
     */
    private long getInstructionCode(final int offset, final int codeLength) {
        return memory.getBytesAsLong(proxy.getSegment(), offset, codeLength);
    }
}
