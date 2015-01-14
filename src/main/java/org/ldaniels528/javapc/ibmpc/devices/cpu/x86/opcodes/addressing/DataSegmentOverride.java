package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Register16bit;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Represents a Data Segment Override Instruction (e.g. "CS:", "DS:", "ES:" or "SS:")
 *
 * @author lawrence.daniels@gmail.com
 */
public class DataSegmentOverride extends AbstractOpCode {
    public final X86Register16bit register;
    public final OpCode instruction;

    /**
     * Creates a new segment register override construct
     *
     * @param register    the given {@link X86Register16bit segment register}
     * @param instruction the given {@link OpCode data instruction}
     */
    public DataSegmentOverride(final X86Register16bit register, final OpCode instruction) {
        this.register = register;
        this.instruction = instruction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        // set the DS-override register
        cpu.setOverrideRegister(register);

        // interpret the next instruction
        cpu.execute(system, instruction);

        // reset the DS override
        cpu.resetOverrideRegister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s: %s", register, instruction);
    }

}
