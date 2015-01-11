package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.system.IbmPcSystem;

/**
 * Compare String Byte
 * <pre>
 * Usage:  CMPS    dest,src
 *         CMPSB
 *         CMPSW
 *         CMPSD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Subtracts destination value from source without saving results.
 * Updates flags based on the subtraction and  the index registers
 * (E)SI and (E)DI are incremented or decremented depending on the
 * state of the Direction Flag. CMPSB inc/decrements the index
 * registers by 1, CMPSW inc/decrements by 2, while CMPSD increments
 * or decrements by 4.  The REP prefixes can be used to process
 * entire data items.
 *
 *                          Clocks                 Size
 * Operands         808x  286   386   486          Bytes
 *
 * dest,src          22    8     10    8             1  (W88=30)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see REPZ
 * @see REPNZ
 */
public class CMPSB extends AbstractOpCode {
    private static CMPSB instance = new CMPSB();

    /**
     * Private constructor
     */
    private CMPSB() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CMPSB getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        // get the RAM instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // compare byte from DS:[SI] to ES:[DI]
        final Operand src = memory.getByte(cpu.DS, cpu.SI);
        final Operand dst = memory.getByte(cpu.ES, cpu.DI);

        // perform the comparison (update flags)
        cpu.FLAGS.updateSUB(dst, src);

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? -1 : 1;

        // increment/decrement SI
        cpu.SI.add(delta);
        cpu.DI.add(delta);
    }

}