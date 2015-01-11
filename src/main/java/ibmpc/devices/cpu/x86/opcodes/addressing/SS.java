package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

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
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
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