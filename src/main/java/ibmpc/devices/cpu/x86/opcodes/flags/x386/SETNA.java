package ibmpc.devices.cpu.x86.opcodes.flags.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * SETBE/SETNA - Set if Below or Equal / Set if Not Above (386+)
 *  Usage:  SETBE   dest
 *  		SETNA   dest
 *  (unsigned, 386+)
 *  Modifies flags: none
 *  
 *  Sets the byte in the operand to 1 if the Carry Flag or the Zero
 *  Flag is set, otherwise sets the operand to 0.
 *  
                             Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *  reg8              -     -     4     3             3
 *  mem8              -     -     5     4             3
 * </pre>
 * @author ldaniels
 */
public class SETNA extends AbstractOpCode {
	private final Operand dest;
	
	/**
	 * Private constructor
	 */
	public SETNA( final Operand dest ) {
		this.dest = dest;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		dest.set( cpu.FLAGS.isCF() || cpu.FLAGS.isZF() ? 1 : 0 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "SETNA %s",dest );
	}

}