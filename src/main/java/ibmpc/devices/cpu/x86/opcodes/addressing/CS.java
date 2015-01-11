package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

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
    public void execute(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
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