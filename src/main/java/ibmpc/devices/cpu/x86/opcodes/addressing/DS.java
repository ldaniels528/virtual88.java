package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

/**
 * DS:
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

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		override( cpu, cpu.DS );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return "DS:";
	}

}