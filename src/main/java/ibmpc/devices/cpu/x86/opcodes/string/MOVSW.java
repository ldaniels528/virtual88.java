package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

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
public class MOVSW extends AbstractOpCode {
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
    public void execute(final Intel80x86 cpu) {
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

}
