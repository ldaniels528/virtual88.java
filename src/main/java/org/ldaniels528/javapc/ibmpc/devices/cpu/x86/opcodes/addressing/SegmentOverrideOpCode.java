package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Segment Override OpCode (i.e. 'CS:', 'ES:', 'DS:', or 'SS:')
 *
 * @author lawrence.daniels@gmail.com
 */
public abstract class SegmentOverrideOpCode extends AbstractOpCode {

    /**
     * Default constructor
     */
    protected SegmentOverrideOpCode() {
        super();
    }

    /**
     * Provides a means to override the the next instruction using a segment register
     *
     * @param system   the given {@link org.ldaniels528.javapc.ibmpc.system.IbmPcSystem IBM PC system}
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86 Intel 8086 CPU}
     * @param register the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register 8086 register}
     * @throws X86AssemblyException
     */
    protected void override(final IbmPcSystem system, final Intel80x86 cpu, final X86Register register) throws X86AssemblyException {
        // save the current DS
        final int segment = cpu.DS.get();

        // set DS to the referenced register
        cpu.DS.set(register.get());

        // interpret the next instruction
        final OpCode opCode = cpu.getNextOpCode();
        cpu.execute(system, opCode);

        // set DS back to it's original value
        cpu.DS.set(segment);
    }

}
