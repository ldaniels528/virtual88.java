package ibmpc.devices.cpu.x86.opcodes.flow.jump;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractFlowControlOpCode;

/**
 * <pre>
 * Jump if Lower or Equal
 * Jump Condition: ZF=1 or SF != OF
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class JLE extends AbstractFlowControlOpCode {
	
	/**
	 * Creates a new conditional jump instruction
	 * @param offset the given memory offset to jump to.
	 */
	public JLE( final Operand offset ) {
		super( offset );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean redirectsFlow( final Intel80x86 cpu ) {
		return ( cpu.FLAGS.isZF() || ( cpu.FLAGS.isSF() != cpu.FLAGS.isOF() ) );
	}

}