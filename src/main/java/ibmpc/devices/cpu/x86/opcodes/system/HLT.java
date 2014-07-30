package ibmpc.devices.cpu.x86.opcodes.system;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Usage:   HLT
 * Modifies flags: None
 *
 * Halts CPU until RESET line is activated, NMI or maskable interrupt
 * received.  The CPU becomes dormant but retains the current CS:IP
 * for later restart.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class HLT extends AbstractOpCode {
	private static HLT instance = new HLT();
	
	/**
	 * Private constructor
	 */
	private HLT() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static HLT getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		cpu.halt();
	}

}
