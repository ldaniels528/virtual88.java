package ibmpc.devices.cpu.x86.opcodes.system;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

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
 * @author lawrence.daniels@gmail.com
 *
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
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// TODO figure it out
	}

}