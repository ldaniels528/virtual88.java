package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Represents an opCode that is capable of being used
 * in conjunction with repeat instructions to move, compare,
 * or scan mass amounts of data.
 *
 * @author ldaniels
 */
public interface MassDataOpCode extends OpCode {

    /**
     * Executes the mass data opCode
     *
     * @param cpu   the {@link Intel80x86 Intel 8086} instance
     * @param count the number of data elements to operate upon
     * @throws X86AssemblyException
     */
    void executeEnMass(Intel80x86 cpu, int count);

}
