package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Set Direction Flag
 * 
 * Usage:  STD
 * Modifies flags: DF
 *
 * Sets the Direction Flag to 1.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class STD extends AbstractOpCode {
	private static STD instance = new STD();
	
	/**
	 * Private constructor
	 */
	private STD() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static STD getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		cpu.FLAGS.setDF( true );
	}

}
