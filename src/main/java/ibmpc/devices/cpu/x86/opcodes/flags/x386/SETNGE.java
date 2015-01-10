package ibmpc.devices.cpu.x86.opcodes.flags.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * SETNGE
 * Set if not greater or equal (SF<>OF)
 *
 * @author ldaniels
 */
public class SETNGE extends AbstractOpCode {
    private final Operand dest;

    /**
     * Private constructor
     */
    public SETNGE(final Operand dest) {
        this.dest = dest;
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        dest.set(cpu.FLAGS.isSF() != cpu.FLAGS.isOF() ? 1 : 0);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
     */
    public String toString() {
        return String.format("SETNGE %s", dest);
    }

}