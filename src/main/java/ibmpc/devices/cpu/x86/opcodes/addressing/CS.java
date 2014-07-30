package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

/**
 * CS:
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

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		override( cpu, cpu.CS );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return "CS:";
	}

}