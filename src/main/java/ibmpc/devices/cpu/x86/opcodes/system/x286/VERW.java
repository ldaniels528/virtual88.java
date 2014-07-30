package ibmpc.devices.cpu.x86.opcodes.system.x286;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * VERW - Verify Write (286+ protected)
 * <pre>
 *	  Usage:  VERW    src
 *    Modifies flags: ZF
 *
 *    Verifies the specified segment selector is valid and is ratable
 *    at the current privilege level.  If the segment is writable,
 *    the Zero Flag is set, otherwise it is cleared.
 *
 *                             Clocks                 Size
 *    Operands         808x  286   386   486          Bytes
 *    reg16             -     14    15    11            3
 *    mem16             -     16    16    11            5
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class VERW extends AbstractOpCode {
	private final Operand src;
	
	/**
	 * VERW src
	 * @param src the given source {@link Operand operand}
	 */
	public VERW( final Operand src ) {
		this.src = src;
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
		return String.format( "VERW %s", src );
	}
	
}
