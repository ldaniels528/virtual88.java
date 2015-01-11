package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * 	Usage:  STOS    dest
 *          STOSB
 *          STOSW
 *          STOSD
 *  Modifies flags: None
 *
 *  Stores value in accumulator to location at ES:(E)DI (even if operand
 *  is given).  (E)DI is incremented/decremented based on the size of
 *  the operand (or instruction format) and the state of the Direction
 *  Flag.   Use with REP prefixes.
 *
 *                           Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *
 *  dest              11    3     4     5             1  (W88=15)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see REPZ
 * @see REPNZ
 */
public class STOSB extends AbstractOpCode {
    private static STOSB instance = new STOSB();

    /**
     * Private constructor
     */
    private STOSB() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static STOSB getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        // get the RAM instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // put byte from AL into DS:[SI]
        memory.setByte(cpu.DS.get(), cpu.SI.get(), cpu.AL.get());

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? -1 : 1;

        // increment/decrement SI
        cpu.SI.add(delta);
    }

}