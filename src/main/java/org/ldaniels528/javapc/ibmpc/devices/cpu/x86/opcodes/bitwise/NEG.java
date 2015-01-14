package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractSingleOperandOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

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
public class NEG extends AbstractSingleOperandOpCode {

    /**
     * NEG operand
     *
     * @param operand the given {@link Operand operand}
     */
    public NEG(final Operand operand) {
        super("NEG", operand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu)
            throws X86AssemblyException {
        // get the initial value of the destination
        final int value0 = operand.get();

        // compute 2's complement
        final int value1 = (2 - value0);

        // set the desination with the new value
        operand.set(value1);
    }

}