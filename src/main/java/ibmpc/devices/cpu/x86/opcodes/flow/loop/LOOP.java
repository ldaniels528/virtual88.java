package ibmpc.devices.cpu.x86.opcodes.flow.loop;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * LOOP destination
 * @author lawrence.daniels@gmail.com
 */
public class LOOP extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional loop instruction
	 * @param destination the given memory offset to jump to.
	 */
	public LOOP( final Operand destination ) {
		super( destination );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.ConditionalOpCode#redirectsFlow(ibmpc.devices.cpu.Intel80x86)
	 */
	protected boolean redirectsFlow( final Intel80x86 cpu ) {
		final boolean ok = ( cpu.CX.get() > 0 );
		if( ok ) {
			// decrement cx
			cpu.CX.add( -1 );
		}
		return ok;
	}

}