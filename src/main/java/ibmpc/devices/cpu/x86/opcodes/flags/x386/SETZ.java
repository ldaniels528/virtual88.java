package ibmpc.devices.cpu.x86.opcodes.flags.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * SETE/SETZ - Set if Equal / Set if Zero (386+)
 *
 * 	Usage:  SETE    dest
 *         	SETZ    dest
 * 	Modifies flags: none
 *
 * 	Sets the byte in the operand to 1 if the Zero Flag is set,
 *  otherwise sets the operand to 0.
 *
 *                           Clocks                 Size
 * 	Operands         808x  286   386   486          Bytes
 *  reg8              -     -     4     3             3
 *  mem8              -     -     5     4             3
 * </pre>
 *
 * @author ldaniels
 */
public class SETZ extends AbstractOpCode {
    private final Operand dest;

    /**
     * Private constructor
     */
    public SETZ(final Operand dest) {
        this.dest = dest;
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        dest.set(cpu.FLAGS.isZF() ? 1 : 0);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
     */
    public String toString() {
        return String.format("SETZ %s", dest);
    }

}