package ibmpc.devices.cpu.x86.opcodes.flow.loop;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * LOOPNZ label
 * @author lawrence.daniels@gmail.com
 */
public class LOOPNZ extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional loop instruction
	 * @param destination the given offset code.
	 */
	public LOOPNZ( final Operand destination ) {
		super( destination );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.ConditionalOpCode#redirectsFlow(ibmpc.devices.cpu.Intel80x86)
	 */
	protected boolean redirectsFlow( final Intel80x86 cpu ) {
		final boolean ok = ( cpu.CX.get() > 0 ) && ( !cpu.FLAGS.isZF() );
		if( ok ) {
			// decrement cx
			cpu.CX.add( -1 );
		}
		return ok;
	}

}