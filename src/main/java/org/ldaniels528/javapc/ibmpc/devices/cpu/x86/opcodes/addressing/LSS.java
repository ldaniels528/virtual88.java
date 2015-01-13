package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Load Pointer Using SS (LSS)
 *
 * Usage: LSS dest,src
 * Modifies Flags: None
 *
 * Loads 32-bit pointer from memory source to destination
 * register and SS. The offset is placed in the destination
 * register and the segment is placed in SS. To use this
 * instruction the word at the lower memory address must
 * contain the offset and the word at the higher address
 * must contain the segment. This simplifies the loading
 * of far pointers from the stack and the interrupt vector table.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class LSS extends AbstractOpCode {
    private final X86Register dest;
    private final MemoryReference src;

    /**
     * LSS dst, src (e.g. 'LSS AX,[BX+SI]')
     *
     * @param dest the given {@link X86Register destination}
     * @param src  the given {@link MemoryReference source}
     */
    public LSS(final X86Register dest, final MemoryReference src) {
        this.dest = dest;
        this.src = src;
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // get the random access memory (RAM) instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // cache the segment register (SS)
        final X86Register segReg = cpu.SS;

        // get segment and offset
        final int base = src.getOffset();
        final int offset = memory.getWord(segReg.get(), base);
        final int segment = memory.getWord(segReg.get(), base + 2);

        // load the segment and offset into SS:dst
        segReg.set(segment);
        dest.set(offset);
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
     */
    public String toString() {
        return String.format("LSS %s,%s", dest, src);
    }

}
