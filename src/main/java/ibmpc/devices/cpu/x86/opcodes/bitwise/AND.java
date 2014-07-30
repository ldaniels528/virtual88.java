package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * Logical AND
 * <pre>
 *   Usage:  AND     dest,src
 *   Modifies flags: CF OF PF SF ZF (AF undefined)
 *
 *        Performs a logical AND of the two operands replacing the destination
 *        with the result.
 *       
 *                                Clocks                  Size
 *        Operands         808x  286   386   486          Bytes
 *		  -----------------------------------------------------------------
 *        reg,reg           3     2     2     1             2
 *        mem,reg         16+EA   7     7     3            2-4  (W88=24+EA)
 *        reg,mem          9+EA   7     6     1            2-4  (W88=13+EA)
 *        reg,immed         4     3     2     1            3-4
 *        mem,immed       17+EA   7     7     3            3-6  (W88=23+EA)
 *        accum,immed       4     3     2     1            2-3
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class AND extends AbstractDualOperandOpCode {
	
	/**
	 * AND dest, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public AND( final Operand dest, final Operand src ) {
		super( "AND", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// perform the logical AND operation
		// on the source and destination values
		final int andValue = dest.get() & src.get();
		final int addValue = dest.get() + src.get();

		// set the dest
		dest.set( andValue );
		
		// adjust the flags
		final X86ExtendedFlags flags = cpu.FLAGS;
		flags.setCF( andValue == addValue );
		flags.setOF( andValue > addValue );
		flags.setPF( andValue % 2 == 0 );
		flags.setSF( andValue >= 0x80 );
		flags.setZF( andValue == 0 );
	}

}