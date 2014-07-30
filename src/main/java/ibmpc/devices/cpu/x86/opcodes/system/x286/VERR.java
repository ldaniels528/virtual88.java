package ibmpc.devices.cpu.x86.opcodes.system.x286;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * VERR - Verify Read (286+ protected)
 * <pre>
 *		Usage:  VERR    src
 *      Modifies flags: ZF
 *
 *      Verifies the specified segment selector is valid and is readable
 *      at the current privilege level.  If the segment is readable,
 *      the Zero Flag is set, otherwise it is cleared.
 *
 *                               Clocks                 Size
 *      Operands         808x  286   386   486          Bytes
 *      reg16             -     14    10    11            3
 *      mem16             -     16    11    11            5
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class VERR extends AbstractOpCode {
	private final Operand src;
	
	/**
	 * VERR src
	 * @param dest the given source {@link Operand operand}
	 */
	public VERR( final Operand dest ) {
		this.src = dest;
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
		return String.format( "VERR %s", src );
	}
	
}
