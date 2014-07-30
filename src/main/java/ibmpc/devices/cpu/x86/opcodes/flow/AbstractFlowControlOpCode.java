package ibmpc.devices.cpu.x86.opcodes.flow;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Represents a generic "branching" opCode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractFlowControlOpCode extends AbstractOpCode {
	protected final Operand destination;
	
	/**
	 * Creates a new flow control opCode
	 * @param destination the given {@link Operand destination}
	 */
	public AbstractFlowControlOpCode( final Operand destination ) {
		this.destination = destination;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		if( redirectsFlow( cpu ) ) {
			cpu.jumpTo( this, destination, false );
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#isConditional()
	 */
	public boolean isConditional() {
		return true;
	}
	
	/**
	 * Indicates whether the result of the opCode
	 * is "code fallthru" meaning no adjustment
	 * is need to the queued opCodes.
	 * @param cpu the given {@link Intel80x86 Intel¨ 8086 CPU}
	 * @return the result of evaluate the opCode's condition
	 */
	protected abstract boolean redirectsFlow( Intel80x86 cpu );
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "%s %s", getClass().getSimpleName(), destination );
	}
	
}
