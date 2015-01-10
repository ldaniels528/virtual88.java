package ibmpc.devices.cpu.x86.opcodes.string.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Scan String Double-Word
 * <pre>
 * Usage:  SCAS    string
 *         SCASB
 *         SCASW
 *         SCASD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Compares value at ES:DI (even if operand is specified) from the
 * accumulator and sets the flags similar to a subtraction.  DI is
 * incremented/decremented based on the instruction format (or
 * operand size) and the state of the Direction Flag.  Use with REP
 * prefixes.
 * </pre>
 * @see <a href="https://courses.engr.illinois.edu/ece390/archive/spr2002/books/labmanual/inst-ref-scasb.html">SCASB</a>
 */
public class SCASD extends AbstractOpCode {
    private static SCASD instance = new SCASD();

    /**
     * Private constructor
     */
    private SCASD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static SCASD getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        // get the register collection and memory instances
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // get the double word from register AL
        final int data0 = cpu.EAX.get();

        // put the double word into ES:[DI]
        final int data1 = memory.getDoubleWord(cpu.ES.get(), cpu.DI.get());

        // compare the data
        cpu.FLAGS.compare(data0, data1);

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? 4 : -4;

        // increment/decrement pointers
        cpu.DI.add(delta);
    }

}