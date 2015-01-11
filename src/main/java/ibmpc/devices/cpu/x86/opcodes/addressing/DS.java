package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * DS:
 *
 * @author lawrence.daniels@gmail.com
 */
public class DS extends SegmentOverrideOpCode {
    private static DS instance = new DS();

    /**
     * Private constructor
     */
    private DS() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static DS getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
        override(system, cpu, cpu.DS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DS:";
    }

}