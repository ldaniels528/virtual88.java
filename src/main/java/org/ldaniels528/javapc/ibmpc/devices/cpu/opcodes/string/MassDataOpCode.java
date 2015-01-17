package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.string;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;

/**
 * Represents an opCode that is capable of being used
 * in conjunction with repeat instructions to move, compare,
 * or scan mass amounts of data.
 *
 * @author ldaniels
 */
public interface MassDataOpCode {

    /**
     * Executes the mass data opCode
     *
     * @param cpu   the {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 Intel 8086} instance
     * @param count the number of data elements to operate upon
     */
    void executeEnMass(I8086 cpu, int count);

}
