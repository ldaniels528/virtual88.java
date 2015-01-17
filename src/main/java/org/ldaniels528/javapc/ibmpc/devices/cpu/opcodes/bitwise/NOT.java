package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractSingleOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
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
public class NOT extends AbstractSingleOperandOpCode {

    /**
     * NOT operand
     *
     * @param operand the given {@link Operand operand}
     */
    public NOT(final Operand operand) {
        super("NOT", operand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        // compute 1's complement (e.g. AB (10101011) becomes 54 (01010100))
        final int value1 = (1 - operand.get());

        // set the operand with the new value
        operand.set(value1);
    }

}
