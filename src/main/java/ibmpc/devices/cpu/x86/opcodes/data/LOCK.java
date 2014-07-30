package ibmpc.devices.cpu.x86.opcodes.data;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Syntax: LOCK
 * 
 * Locks the BUS
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class LOCK extends AbstractOpCode {
	private static LOCK instance = new LOCK();
	
	/**
	 * Private constructor
	 */
	private LOCK() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static LOCK getInstance() {
		return instance;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		// TODO determine how to lock the bus
	}

}
