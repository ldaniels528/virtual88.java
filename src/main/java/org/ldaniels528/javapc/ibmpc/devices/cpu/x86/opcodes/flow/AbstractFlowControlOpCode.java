package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Represents a generic "branching" opCode
 *
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractFlowControlOpCode extends AbstractOpCode {
    protected final Operand destination;

    /**
     * Creates a new flow control opCode
     *
     * @param destination the given {@link Operand destination}
     */
    public AbstractFlowControlOpCode(final Operand destination) {
        this.destination = destination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
        if (redirectsFlow(cpu)) {
            cpu.jumpTo(this, destination, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConditional() {
        return true;
    }

    /**
     * Indicates whether the result of the opCode
     * is "code fallthru" meaning no adjustment
     * is need to the queued opCodes.
     *
     * @param cpu the given {@link Intel80x86 Intel 8086 CPU}
     * @return the result of evaluate the opCode's condition
     */
    protected abstract boolean redirectsFlow(Intel80x86 cpu);

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), destination);
    }

}
