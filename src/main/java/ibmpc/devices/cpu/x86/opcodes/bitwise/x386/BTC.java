package ibmpc.devices.cpu.x86.opcodes.bitwise.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * BTC -- Bit Test and Complement
 * <pre>
 * Opcode        Instruction     Clocks  Description
 * 0F  BB        BTC r/m16,r16   6/13    Save bit in carry flag and complement
 * 0F  BB        BTC r/m32,r32   6/13    Save bit in carry flag and complement
 * 0F  BA /7 ib  BTC r/m16,imm8  6/8     Save bit in carry flag and complement
 * 0F  BA /7 ib  BTC r/m32,imm8  6/8     Save bit in carry flag and complement
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class BTC extends AbstractDualOperandOpCode {
	
	/**
	 * Default constructor
	 */
	public BTC( final Operand dest, final Operand src ) {
		super( "BT", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		throw new IllegalStateException( "Not yet implemented" );
	}

}
