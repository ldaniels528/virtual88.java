package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes;

import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;

/**
 * Represents a generic opCode
 *
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractOpCode implements OpCode {
    private long instructionCode;
    private int length;

    /**
     * Default constructor
     */
    protected AbstractOpCode() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConditional() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isForcedRedirect() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getInstructionCode() {
        return instructionCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInstructionCode(final long instructionCode) {
        this.instructionCode = instructionCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
