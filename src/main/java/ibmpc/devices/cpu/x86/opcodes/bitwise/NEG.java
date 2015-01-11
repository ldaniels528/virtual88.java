package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  NEG     dest
 * Modifies flags: AF CF OF PF SF ZF
 *
 *  Subtracts the destination from 0 and saves the 2s complement of
 *  "dest" back into "dest".
 *
 *                         Clocks             		Size
 * 	Operands         808x  286   386   486          Bytes
 *
 * 	reg               3     2     2     1             2
 * 	mem             16+EA   7     6     3            2-4  (W88=24+EA)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class NEG extends AbstractOpCode {
    private final Operand dest;

    /**
     * NEG dest
     *
     * @param operand
     */
    public NEG(final Operand operand) {
        this.dest = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        // get the initial value of the destination
        final int value0 = dest.get();

        // compute 2's complement
        final int value1 = (2 - value0);

        // set the desination with the new value
        dest.set(value1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("NEG %s", dest);
    }

}