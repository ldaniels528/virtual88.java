package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * 	Usage:  NOT     dest
 * 	Modifies flags: None
 *
 * 	Inverts the bits of the "dest" operand forming the 1s complement.
 *
 *                         Clocks                	Size
 * 	Operands         808x  286   386   486          Bytes
 *
 * 	reg               3     2     2     1             2
 * 	mem             16+EA   7     6     3            2-4  (W88=24+EA)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class NOT extends AbstractOpCode {
    private final Operand dest;

    /**
     * NOT dest
     *
     * @param operand
     */
    public NOT(final Operand operand) {
        this.dest = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu)
            throws X86AssemblyException {
        // get the initial value of the destination
        final int value0 = dest.get();

        // compute 1's complement (e.g. AB (10101011) becomes 54 (01010100))
        final int value1 = (1 - value0);

        // set the desination with the new value
        dest.set(value1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("NOT %s", dest);
    }

}
