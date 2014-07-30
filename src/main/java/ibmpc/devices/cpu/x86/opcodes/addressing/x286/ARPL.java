package ibmpc.devices.cpu.x86.opcodes.addressing.x286;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;

/**
 * <pre>
 * ARPL -- Adjust RPL Field of Selector
 * 
 * The ARPL instruction has two operands. The first operand is 
 * a 16-bit memory variable or word register that contains the 
 * value of a selector. The second operand is a word register. 
 * If the RPL field ("requested privilege level"--bottom two bits) 
 * of the first operand is less than the RPL field of the second operand, 
 * the zero flag is set to 1 and the RPL field of the first operand is 
 * increased to match the second operand. Otherwise, the zero flag is 
 * set to 0 and no change is made to the first operand.
 * 
 * ARPL appears in operating system software, not in application programs. 
 * It is used to guarantee that a selector parameter to a subroutine does 
 * not request more privilege than the caller is allowed. The second operand 
 * of ARPL is normally a register that contains the CS selector value of 
 * the caller.
 * 
 * Opcode    Instruction          Clocks    Description
 * 63 /r     ARPL r/m16,r16       pm=20/21  Adjust RPL of r/m16 to not less than RPL of r16
 * 
 * Operation
 * IF RPL bits(0,1) of DEST < RPL bits(0,1) of SRC
 * THEN
 *	ZF := 1;
 *   RPL bits(0,1) of DEST := RPL bits(0,1) of SRC;
 * ELSE
 * 	ZF := 0;
 * FI;
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class ARPL extends AbstractDualOperandOpCode {
	
	/**
	 * ARPL dst, src (e.g. 'ARPL AX,CX')
	 * @param dest the given {@link X86Register destination}
	 * @param src the given {@link Operand source}
	 */
	public ARPL( final X86Register dest, final Operand src ) {
		super( "ARPL", dest, src );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the value of the destination
		final int valDest = dest.get();
		
		// get the RPL bits (0-1) for the source and destination
		// (mask = 0000 0000 0000 0011 -> 0003h) 
		final int rplDest = ( valDest & 0x0003 );
		final int rplSrc  = ( src.get() & 0x0003 );
		
		// perform the comparison
		final boolean isLess = ( rplDest < rplSrc );
		
		// if the RPL of the destination is less 
		// than the RPL of the source
		if( isLess ) {
			// mask off bits 0-1 on the destination value
			// then OR the RPL bits of the source
			// (mask = 1111 1111 1111 1100 -> FFFCh) 
			final int value = ( valDest & 0xFFFC ) | rplSrc;
		
			// put the value into the destination
			dest.set( value );
		}
		
		// set ZF with the result
		cpu.FLAGS.setZF( isLess );
	}

}
