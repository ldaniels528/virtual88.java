package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * CS:
 *
 * @author lawrence.daniels@gmail.com
 */
public class CS extends SegmentOverrideOpCode {
    private static CS instance = new CS();

    /**
     * Private constructor
     */
    private CS() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CS getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) throws X86AssemblyException {
        override(system, cpu, cpu.CS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CS:";
    }

}