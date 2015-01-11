package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * Represents an 8086 Machine Code Decoder
 *
 * @author lawrence.daniels@gmail.com
 */
public interface Decoder {

    /**
     * Decodes the next byte(s) from the instruction pointer
     * into an 80x86 opCode.
     *
     * @param cpu   the given {@link Intel80x86 Virtual 8086 CPU}
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @return an 80x86 {@link OpCode opCode}
     */
    OpCode decode(Intel80x86 cpu, X86MemoryProxy proxy);

}
