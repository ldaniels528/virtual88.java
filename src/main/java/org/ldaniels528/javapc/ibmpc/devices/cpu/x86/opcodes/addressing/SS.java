package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * SS:
 *
 * @author lawrence.daniels@gmail.com
 */
public class SS extends SegmentOverrideOpCode {
    private static SS instance = new SS();

    /**
     * Private constructor
     */
    private SS() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static SS getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu)
            throws X86AssemblyException {
        override(system, cpu, cpu.SS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SS:";
    }

}