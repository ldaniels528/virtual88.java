package ibmpc.devices.cpu.x86.opcodes.string.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.opcodes.string.REPNZ;
import ibmpc.devices.cpu.x86.opcodes.string.REPZ;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

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
public class LODSD extends AbstractOpCode {
    private static LODSD instance = new LODSD();

    /**
     * Private constructor
     */
    private LODSD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static LODSD getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        // get the RAM instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // load byte at DS:[SI] into AX
        cpu.AX.set(memory.getWord(cpu.DS.get(), cpu.SI.get()));

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? -4 : 4;

        // increment/decrement SI
        cpu.SI.add(delta);
    }

}