package ibmpc.devices.cpu.x86.opcodes.math;

import static ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * Compare (CMP)
 * <pre>
 * Usage:  CMP     dest,src
 * Modifies flags: AF CF OF PF SF ZF
 * 
 * Subtracts source from destination and updates the flags but does
 * not save result.  Flags can subsequently be checked for conditions.
 * 
 *                               Clocks                 Size
 *      Operands         808x  286   386   486          Bytes
 *
 *      reg,reg           3     2     2     1             2
 *      mem,reg          9+EA   7     5     2            2-4  (W88=13+EA)
 *      reg,mem          9+EA   6     6     2            2-4  (W88=13+EA)
 *      reg,immed         4     3     2     1            3-4
 *      mem,immed       10+EA   6     5     2            3-6  (W88=14+EA)
 *      accum,immed       4     3     2     1            2-3
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class CMP extends AbstractDualOperandOpCode {

	/**
	 * CMP dest, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public CMP( final Operand dest, final Operand src ) {
		super( "CMP", dest, src );
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		// cache the values (registers are slower)
		final int value1 = dest.get();
		final int value0 = src.get(); 

		// subtract the source from the destination
		final int diff0   = dest.size() == SIZE_16BIT ? ( value1 - value0 ) & 0xFFFF : value1 - value0;
		
		// update the flags
		final X86ExtendedFlags flags = cpu.FLAGS;
		flags.setCF( value1 < value0 );
		flags.setOF( diff0 > 0xFFFF );
		flags.setZF( diff0 == 0 );
		flags.setSF( value1 < value0 );
		flags.setPF( diff0 % 2 == 0 );
		//flags.setAF( value == 0 );
	}

}

