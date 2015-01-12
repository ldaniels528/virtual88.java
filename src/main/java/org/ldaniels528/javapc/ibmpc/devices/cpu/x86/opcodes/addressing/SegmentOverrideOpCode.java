package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Register16bit;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Segment Override OpCode (e.g. 'CS:', 'ES:', 'DS:', or 'SS:')
 *
 * @author lawrence.daniels@gmail.com
 */
public abstract class SegmentOverrideOpCode extends AbstractOpCode {

    /**
     * Provides a means to override the the next instruction using a segment register
     *
     * @param system   the given {@link org.ldaniels528.javapc.ibmpc.system.IbmPcSystem IBM PC system}
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 Intel 8086 CPU}
     * @param register the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register 8086 register}
     * @throws X86AssemblyException
     */
    protected void override(final IbmPcSystem system,
                            final Intel8086 cpu,
                            final X86Register16bit register) throws X86AssemblyException {
        // set the DS-override register
        cpu.setOverrideRegister(register);

        // interpret the next instruction
        cpu.execute(system, cpu.getNextOpCode());

        // reset the DS override
        cpu.resetOverrideRegister();
    }

}
