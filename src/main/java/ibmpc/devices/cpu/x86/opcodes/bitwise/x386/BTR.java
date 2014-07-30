package ibmpc.devices.cpu.x86.opcodes.bitwise.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * BTR -- Bit Test and Reset
 * <pre>
 * Opcode        Instruction     Clocks  Description
 * 0F  B3        BTR r/m16,r16   6/13    Save bit in carry flag and reset
 * 0F  B3        BTR r/m32,r32   6/13    Save bit in carry flag and reset
 * 0F  BA /6 ib  BTR r/m16,imm8  6/8     Save bit in carry flag and reset
 * 0F  BA /6 ib  BTR r/m32,imm8  6/8     Save bit in carry flag and reset
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class BTR extends AbstractDualOperandOpCode {
	
	/**
	 * Default constructor
	 */
	public BTR( final Operand dest, final Operand src ) {
		super( "BTR", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		throw new IllegalStateException( "Not yet implemented" );
	}

}
