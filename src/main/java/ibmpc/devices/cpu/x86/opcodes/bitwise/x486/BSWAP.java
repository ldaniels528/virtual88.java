package ibmpc.devices.cpu.x86.opcodes.bitwise.x486;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * BSWAP - Byte Swap       (486+)
 * <pre>
 *	  Usage:  BSWAP   reg32
 *    Modifies flags: none
 *
 *    Changes the byte order of a 32 bit register from big endian to
 *    little endian or vice versa.   Result left in destination register
 *    is undefined if the operand is a 16 bit register.
 *
 *                             Clocks                 Size
 *    Operands         808x  286   386   486          Bytes
 *
 *    reg32             -     -     -     1             2
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class BSWAP extends AbstractDualOperandOpCode {
	
	/**
	 * Default constructor
	 */
	public BSWAP( final Operand dest, final Operand src ) {
		super( "BSWAP", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		throw new IllegalStateException( "Not yet implemented" );
	}

}
