package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * DEC <i>target</i>
 *
 * @author lawrence.daniels@gmail.com
 */
public class DEC extends AbstractOpCode {
    private final Operand operand;

    /**
     * Creates a new increment instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public DEC(final Operand operand) {
        this.operand = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        operand.set(cpu.FLAGS.updateDEC(operand));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("DEC %s", operand);
    }

}