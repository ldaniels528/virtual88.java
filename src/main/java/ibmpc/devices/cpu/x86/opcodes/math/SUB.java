package ibmpc.devices.cpu.x86.opcodes.math;

import static ibmpc.devices.cpu.operands.Operand.*;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * SUB dest, src
 * @author lawrence.daniels@gmail.com
 */
public class SUB extends AbstractDualOperandOpCode {
	
	/**
	 * SUB dest, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public SUB( final Operand dest, final Operand src ) {
		super( "SUB", dest, src );
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
		int diff0 = ( value1 - value0 );
		switch( dest.size() ) {
			case SIZE_8BIT: diff0  &= 0xFF; break;
			case SIZE_16BIT: diff0  &= 0xFFFF; break;
		}
		
		// update the flags
		final X86ExtendedFlags flags = cpu.FLAGS;
		flags.setCF( value1 < value0 );
		flags.setOF( diff0 > 0xFFFF );
		flags.setZF( diff0 == 0 );
		flags.setSF( value1 < value0 );
		flags.setPF( diff0 % 2 == 0 );
		//flags.setAF( value == 0 );
		
		// set the dest
		dest.set( diff0 );
	}

}
