package ibmpc.devices.cpu.x86.opcodes.flow.jump;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Carry 
 * Jump Condition: CF=1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class JC extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional jump instruction
	 * @param offset the given memory offset to jump to.
	 */
	public JC( final Operand offset ) {
		super( offset );
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean redirectsFlow( final Intel80x86 cpu ) {
		return cpu.FLAGS.isCF();
	}

}