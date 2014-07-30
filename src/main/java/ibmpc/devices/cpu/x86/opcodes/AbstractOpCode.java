package ibmpc.devices.cpu.x86.opcodes;

import ibmpc.devices.cpu.OpCode;

/**
 * Represents a generic opCode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractOpCode implements OpCode {
	private long instructionCode;
	private int length;
	
	/**
	 * Default constructor
	 */
	protected AbstractOpCode() {
		super();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#isConditional()
	 */
	public boolean isConditional() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#isForcedRedirect()
	 */
	public boolean isForcedRedirect() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#getInstructionCode()
	 */
	public long getInstructionCode() {
		return instructionCode;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#setInstructionCode(long)
	 */
	public void setInstructionCode( final long instructionCode ) {
		this.instructionCode = instructionCode;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#getLength()
	 */
	public int getLength() {
		return length;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#setLength(int)
	 */
	public void setLength( final int length ) {
		this.length = length;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getSimpleName();
	}

}
