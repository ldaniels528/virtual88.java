package ibmpc.devices.cpu.x86.decoder;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Flow Control Callback OpCode
 * @author lawrence.daniels@gmail.com
 */
public class FlowControlCallBackOpCode implements OpCode {
	private final DecodeProcessor processor;
	private final OpCode opCode;
	
	/**
	 * Creates a new flow control callback opCode
	 * @param processor the given {@link DecodeProcessor decode processor}
	 * @param opCode the given host {@link OpCode opCode}
	 */
	public FlowControlCallBackOpCode( final DecodeProcessor processor, final OpCode opCode ) {
		this.processor	= processor;
		this.opCode 	= opCode;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// capture the initial CS and IP
		final int cs0 = cpu.CS.get();
		final int ip0 = cpu.IP.get();
		
		// execute the opCode
		opCode.execute( cpu );
		
		// capture the POST-processed CS and IP
		final int cs1 = cpu.CS.get();
		final int ip1 = cpu.IP.get();
		
		// has the code position changed?
		if( ( cs0 != cs1 ) || ( ip0 != ip1 ) ) {
			// perform the call back
			processor.redirect( cs1, ip1 );
		}
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#getLength()
	 */
	public int getLength() {
		return opCode.getLength();
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#isConditional()
	 */
	public boolean isConditional() {
		return opCode.isConditional();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#forceRedirect()
	 */
	public boolean isForcedRedirect() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#setLength(int)
	 */
	public void setLength( final int length ) {
		opCode.setLength( length );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "%s  (Hooked)", opCode );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#getInstructionCode()
	 */
	public long getInstructionCode() {
		return opCode.getInstructionCode();
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#setInstructionCode(long)
	 */
	public void setInstructionCode(long instructionCode) {
		opCode.setInstructionCode( instructionCode );
	}

}
