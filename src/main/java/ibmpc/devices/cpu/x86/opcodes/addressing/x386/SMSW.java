package ibmpc.devices.cpu.x86.opcodes.addressing.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * SMSW -- Store Machine Status Word
 * 
 * Description
 * SMSW stores the machine status word (part of CR0) 
 * in the two-byte register or memory location indicated 
 * by the effective address operand.
 * 
 * Opcode      Instruction     Clocks          Description
 * 0F  01 /4   SMSW r/m16      2/3,pm=2/2      Store machine status word to EA word
 * </pre>
 * @author ldaniels
 */
public class SMSW extends AbstractOpCode {
	private Operand src;
	
	/**
	 * Creates a new SMSW instruction
	 * @param src the given source
	 */
	public SMSW( final Operand src ) {
		this.src = src;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		src.set( cpu.MSW.get() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "SMSW %s", src );
	}

}
