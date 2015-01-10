package ibmpc.devices.cpu.x86.opcodes.flow.jump;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Greater or Equal 
 * Jump Condition: SF == OF
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class JGE extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional jump instruction
	 * @param offset the given memory offset to jump to.
	 */
	public JGE( final Operand offset ) {
		super( offset );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean redirectsFlow(Intel80x86 cpu) {
		return ( cpu.FLAGS.isSF() == cpu.FLAGS.isOF() );
	}

}