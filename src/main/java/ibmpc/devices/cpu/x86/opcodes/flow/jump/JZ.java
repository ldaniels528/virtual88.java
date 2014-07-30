package ibmpc.devices.cpu.x86.opcodes.flow.jump;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Zero 
 * Jump Condition: ZF=1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class JZ extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional jump instruction
	 * @param destination the given memory offset to jump to.
	 */
	public JZ( final Operand destination ) {
		super( destination );
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected boolean redirectsFlow( final Intel80x86 cpu ) {
		return ( cpu.FLAGS.isZF() );
	}

}