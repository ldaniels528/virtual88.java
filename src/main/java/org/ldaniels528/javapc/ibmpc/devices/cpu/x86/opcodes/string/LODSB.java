package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.string;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.LoadStoreOpCode;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage: LODS src
 *        LODSB
 *        LODSW
 *        LODSD (386+)
 *
 * Modifies Flags: None
 *
 * Transfers string element addressed by DS:SI (even if
 * an operand is supplied) to the accumulator. SI is incremented
 * based on the size of the operand or based on the instruction
 * used. If the Direction Flag is set SI is decremented, if
 * the Direction Flag is clear SI is incremented. Use with REP
 * prefixes.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see REPNZ
 * @see REPZ
 */
public class LODSB extends AbstractOpCode implements LoadStoreOpCode {
    private static final LODSB instance = new LODSB();

    /**
     * Private constructor
     */
    private LODSB() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static LODSB getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // get the RAM instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // load byte at DS:[SI] into AL
        cpu.AL.set(memory.getByte(cpu.DS.get(), cpu.SI.get()));

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? -1 : 1;

        // increment/decrement SI
        cpu.SI.add(delta);
    }

}