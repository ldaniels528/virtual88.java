package ibmpc.devices.cpu.x86.opcodes.flow.jump.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Usage:  JECXZ    label
 * Modifies flags: None
 *
 * Causes execution to branch to "label" if register CX is zero.  
 * Uses unsigned comparision.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class JECXZ extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional jump instruction
	 * @param destination the given memory offset to jump to.
	 */
	public JECXZ( final Operand destination ) {
		super( destination );
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode#redirectsFlow(ibmpc.devices.cpu.Intel80x86)
	 */
	@Override
	protected boolean redirectsFlow( final Intel80x86 cpu ) {
		return ( cpu.ECX.get() == 0 );
	}

}