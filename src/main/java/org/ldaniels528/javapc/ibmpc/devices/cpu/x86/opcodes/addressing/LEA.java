package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Load Executive Address (LEA)
 *
 * Usage: LEA dest,src
 * Modifies Flags: None
 *
 * Transfers offset address of "src" to the destination register.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class LEA extends AbstractOpCode {
    private final X86Register dest;
    private final MemoryReference src;

    /**
     * LEA dest, src (e.g. 'LEA AX,[BX+SI]')
     *
     * @param dest the given {@link X86Register destination}
     * @param src  the given {@link MemoryReference source}
     */
    public LEA(final X86Register dest, final MemoryReference src) {
        this.dest = dest;
        this.src = src;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        dest.set(src.getOffset());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("LEA %s,%s", dest, src);
    }

}
