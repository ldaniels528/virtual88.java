package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * DAS
 *
 * @author lawrence.daniels@gmail.com
 */
public class DAS extends AbstractOpCode {
    private static DAS instance = new DAS();

    /**
     * Private constructor
     */
    private DAS() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static DAS getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {

    }

}
