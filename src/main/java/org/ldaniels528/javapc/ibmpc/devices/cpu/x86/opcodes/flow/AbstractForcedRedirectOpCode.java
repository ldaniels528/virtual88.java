/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.OperandValue;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Represents a non-conditional redirecting opCode
 *
 * @author lawrence.daniels@gmail.com
 */
public class AbstractForcedRedirectOpCode extends AbstractOpCode {
    protected final Operand destination;
    private final boolean savePoint;
    private final boolean isDirect;

    /**
     * Creates a new flow control opCode
     *
     * @param destination the given {@link Operand destination}
     * @param savePoint   indicates whether the current code position should be saved.
     */
    public AbstractForcedRedirectOpCode(final Operand destination, final boolean savePoint) {
        this.destination = destination;
        this.savePoint = savePoint;
        this.isDirect = (destination instanceof OperandValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
        cpu.jumpTo(this, destination, savePoint);
    }

    /**
     * @return the destination
     */
    public Operand getDestination() {
        return destination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConditional() {
        return !isDirect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isForcedRedirect() {
        return isDirect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), destination);
    }

}
