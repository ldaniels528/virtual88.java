package ibmpc.devices.cpu.x86.opcodes.system.x286;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * STR - Store Task Register (286+ privileged)
 * <pre>
 *	  Usage:  STR     dest
 *    Modifies flags: None
 *
 *    Stores the current Task Register to the specified operand.
 *
 *                             Clocks                 Size
 *    Operands         808x  286   386   486          Bytes
 *
 *    reg16             -     2     2     2             3
 *    mem16             -     3     2     3             5
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class STR extends AbstractOpCode {
	private final Operand dest;
	
	/**
	 * STR dest
	 * @param dest the given destination {@link Operand operand}
	 */
	public STR( final Operand dest ) {
		this.dest = dest;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		throw new IllegalStateException( "Not yet implemented" );
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "STR %s", dest );
	}
	
}
