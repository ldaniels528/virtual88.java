package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Segment Override OpCode (i.e. 'CS:', 'ES:', 'DS:', or 'SS:')
 * @author lawrence.daniels@gmail.com
 */
public abstract class SegmentOverrideOpCode extends AbstractOpCode {
	
	/**
	 * Default constructor
	 */
	protected SegmentOverrideOpCode() {
		super();
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	protected void override( final Intel80x86 cpu, final X86Register register ) 
	throws X86AssemblyException {
		// save the current DS
		final int segment = cpu.DS.get();
		
		// set DS to the referenced register
		cpu.DS.set( register.get() );
		
		// interpret the next instruction
		final OpCode opCode = cpu.getNextOpCode();
		cpu.execute( opCode );
	
		// set DS back to it's original value
		cpu.DS.set( segment );
	}

}
