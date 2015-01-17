package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  WAIT
 *         FWAIT
 *  Modifies flags: None
 *
 *  CPU enters wait state until the coor signals it has finished
 *  its operation.  This instruction is used to prevent the CPU from
 *  accessing memory that may be temporarily in use by the coor.
 *  WAIT and FWAIT are identical
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class WAIT extends AbstractOpCode {
    private static WAIT instance = new WAIT();

    /**
     * Private constructor
     */
    private WAIT() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static WAIT getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // TODO figure it out
    }

}