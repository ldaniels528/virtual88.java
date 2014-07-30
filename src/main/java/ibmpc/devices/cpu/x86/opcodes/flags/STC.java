package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Set Carry Flag
 * 
 * Usage:  STC
 * Modifies flags: CF
 *
 * Sets the Carry Flag to 1.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class STC extends AbstractOpCode {
	private static STC instance = new STC();
	
	/**
	 * Private constructor
	 */
	private STC() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static STC getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		cpu.FLAGS.setCF( true );
	}

}
