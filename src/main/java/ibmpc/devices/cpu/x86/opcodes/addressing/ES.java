package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

/**
 * ES:
 * @author lawrence.daniels@gmail.com
 */
public class ES extends SegmentOverrideOpCode {
	private static ES instance = new ES();
	
	/**
	 * Private constructor
	 */
	private ES() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static ES getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		override( cpu, cpu.ES );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return "ES:";
	}

}