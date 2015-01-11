package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.string;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryUtil.computePhysicalAddress;
import static org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryUtil.reverseBlock;

/**
 * <pre>
 * Copies data from addressed by DS:SI (even if operands are given) to
 * the location ES:DI destination and updates SI and DI based on the
 * size of the operand or instruction used.  SI and DI are incremented
 * when the Direction Flag is cleared and decremented when the Direction
 * Flag is Set.  Use with REP prefixes.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see REPZ
 * @see REPNZ
 */
public class MOVSW extends AbstractOpCode  implements MassDataOpCode {
    private static MOVSW instance = new MOVSW();

    /**
     * Private constructor
     */
    private MOVSW() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static MOVSW getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        // get the register collection and memory instances
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // get the data word from DS:[SI]
        final int data = memory.getWord(cpu.DS.get(), cpu.SI.get());

        // put the data word into ES:[DI]
        memory.setWord(cpu.ES.get(), cpu.DI.get(), data);

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? -2 : 2;

        // increment/decrement SI and DI
        cpu.SI.add(delta);
        cpu.DI.add(delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeEnMass(final Intel8086 cpu, final int wordCount) {
        // get the register collection and memory instances
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // determine direction
        final boolean backward = cpu.FLAGS.isDF();

        // two bytes per word
        final int count = wordCount * 2;

        // setup increment/decrement value
        final int delta = count * (backward ? -1 : 1);

        // if the direction is backwards ...
        if (backward) {
            // determine the source and destination addresses
            final int sourceAddress = computePhysicalAddress(cpu.DS.get(), cpu.SI.get()) + delta;
            final int destinationAddress = computePhysicalAddress(cpu.ES.get(), cpu.DI.get()) + delta;

            // get the source block from DS:[SI]
            final byte[] srcBlock = memory.getBytes(sourceAddress, count);

            // reverse the bytes in the memory block
            final byte[] dstBlock = reverseBlock(srcBlock);

            // copy the source block into ES:[DI]
            memory.setBytes(destinationAddress, dstBlock, count);
        }

        // must be forward
        else {
            // move "count" bytes of data from DS:[SI] to ES:[DI]
            memory.copyBytes(cpu.DS.get(), cpu.SI.get(), cpu.ES.get(), cpu.DI.get(), count);
        }

        // increment/decrement SI and DI
        cpu.SI.add(delta);
        cpu.DI.add(delta);
    }

}
