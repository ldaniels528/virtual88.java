package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
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

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        final int value = operand.get();
        operand.set(value - 1);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return String.format("DEC %s", operand);
    }

}