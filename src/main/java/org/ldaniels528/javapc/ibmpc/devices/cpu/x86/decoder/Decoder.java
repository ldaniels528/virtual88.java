package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * Represents an 8086 Machine Code Decoder
 *
 * @author lawrence.daniels@gmail.com
 */
public interface Decoder {

    /**
     * Decodes the next byte(s) from the instruction pointer into an 8086 opCode.
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 Virtual 8086 CPU}
     * @param proxy the given {@link org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy memory proxy}
     * @param processor the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecodeProcessor decode processor}
     * @return an 80x86 {@link OpCode opCode}
     */
    OpCode decode(I8086 cpu, X86MemoryProxy proxy, DecodeProcessor processor);

}
