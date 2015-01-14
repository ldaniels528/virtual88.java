package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Flow Control Callback OpCode
 *
 * @author lawrence.daniels@gmail.com
 */
public class FlowControlCallBackOpCode implements OpCode {
    private final DecodeProcessor processor;
    public final OpCode opCode;

    /**
     * Creates a new flow control callback opCode
     *
     * @param processor the given {@link DecodeProcessor decode processor}
     * @param opCode    the given host {@link OpCode opCode}
     */
    public FlowControlCallBackOpCode(final DecodeProcessor processor, final OpCode opCode) {
        this.processor = processor;
        this.opCode = opCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        // capture the initial CS and IP
        final int cs0 = cpu.CS.get();
        final int ip0 = cpu.IP.get();

        // execute the opCode
        opCode.execute(system, cpu);

        // capture the POST-processed CS and IP
        final int cs1 = cpu.CS.get();
        final int ip1 = cpu.IP.get();

        // has the code position changed?
        if ((cs0 != cs1) || (ip0 != ip1)) {
            // perform the call back
            processor.redirect(cs1, ip1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return opCode.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConditional() {
        return opCode.isConditional();
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
    public void setLength(final int length) {
        opCode.setLength(length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return opCode.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getInstructionCode() {
        return opCode.getInstructionCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInstructionCode(long instructionCode) {
        opCode.setInstructionCode(instructionCode);
    }

}
