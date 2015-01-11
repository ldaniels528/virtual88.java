package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Define Byte (DB) MACRO Instruction
 *
 * @author ldaniels
 */
public class DB extends AbstractOpCode {
    private int value;

    /**
     * Creates a new define byte instruction
     *
     * @param byteValue the given byte value
     */
    public DB(final int byteValue) {
        this.value = byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu)
            throws X86AssemblyException {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("DB %02X", value);
    }

}
