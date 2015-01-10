package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

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
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        override(cpu, cpu.SS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SS:";
    }

}