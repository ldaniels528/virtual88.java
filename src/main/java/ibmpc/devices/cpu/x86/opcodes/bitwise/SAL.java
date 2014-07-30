/**
 * 
 */
package ibmpc.devices.cpu.x86.opcodes.bitwise;

import static ibmpc.devices.cpu.operands.Operand.*;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * <pre>
 * Shift Arithmetic Left
 * 
 * 	Usage:  SAL     dest,count
 *	        SHL     dest,count
 *	Modifies flags: CF OF PF SF ZF (AF undefined)
 * </pre>
 * @see SHL
 * @author lawrence.daniels@gmail.com
 */
public class SAL extends AbstractDualOperandOpCode {
	
	/**
	 * SAL dst, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public SAL( final Operand dest, final Operand src ) {
		super( "SAL", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the source and destination values
		final int value0 = dest.get();
		final int value1 = src.get();

		// shift the destination left by the source value
		dest.set( value0 << value1 );
		
		// put the left most bit into CF
		cpu.FLAGS.setCF( ( value0 & getBitMask( dest ) ) != 0 );
	}

	/**
	 * Returns the appropriate mask for determine the left most bit
	 * @param value the given value
	 * @return the mask for determine the left most bit
	 */
	private int getBitMask( final Operand operand ) {
		switch( operand.size() ) {
		case SIZE_8BIT:		return 0x80; 		// 1000 0000
		case SIZE_16BIT:	return 0x8000;		// 1000 0000 0000 0000
		case SIZE_32BIT:	return 0x80000000; 	// 1000 0000 0000 0000 0000 0000 0000 0000
		default: 
			throw new IllegalArgumentException( "Unhandled operand size" );
		}
	}
	
}