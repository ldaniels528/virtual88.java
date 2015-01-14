package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;


import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractSingleOperandOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * INC
 *
 * @author lawrence.daniels@gmail.com
 */
public class INC extends AbstractSingleOperandOpCode {

    /**
     * Creates a new increment instruction
     *
     * @param operand the given {@link Operand operand}
     */
    public INC(final Operand operand) {
        super("INC", operand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        operand.set(cpu.FLAGS.updateINC(operand));
    }

}
