package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/** 
 * <pre>
 * Usage:  ADC     dest,src
 * Modifies flags: AF CF OF SF PF ZF
 *
 * Sums two binary operands placing the result in the destination.
 * If CF is set, a 1 is added to the destination.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class ADC extends AbstractDualOperandOpCode {
	
	/**
	 * ADC dest, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public ADC( final Operand dest, final Operand src ) {
		super( "ADC", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// cache the flags
		final X86ExtendedFlags FLAGS = cpu.FLAGS;
		
		// cache the values (registers are slower)
		final int value0 = dest.get();
		final int value1 = src.get(); 

		// compute the sum
		final int sum = value0 + value1 + ( FLAGS.isCF() ? 1 : 0 );
		
		// calculate the sum of the values,
		// and set the dest
		dest.set( sum );
		
		// adjust the flags
		FLAGS.setOF( ( sum & 0xFFFF0000 ) > 0 );
		FLAGS.setCF( ( sum & 0xFFFF0000 ) > 0 );
		FLAGS.setPF( sum % 2 == 0 );
		FLAGS.setZF( sum == 0 );
	}

}
