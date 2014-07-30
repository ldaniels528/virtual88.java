package ibmpc.devices.cpu.x86.opcodes.bitwise;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;

/**
 * OR dest,src
 * @author lawrence.daniels@gmail.com
 */
public class OR extends AbstractDualOperandOpCode {
	
	/**
	 * OR dest, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public OR( final Operand dest, final Operand src ) {
		super( "OR", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// perform the logical OR operation
		// on the source and destination values
		final int orValue = dest.get() | src.get();
		final int addValue = dest.get() + src.get();

		// set the dest
		dest.set( orValue );
		
		// adjust the flags
		final X86ExtendedFlags flags = cpu.FLAGS;
		flags.setCF( orValue == addValue );
		flags.setOF( orValue > addValue );
		flags.setPF( orValue % 2 == 0 );
		flags.setSF( orValue >= 0x80 );
		flags.setZF( orValue == 0 );
	}
	
}