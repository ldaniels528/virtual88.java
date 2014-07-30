package ibmpc.devices.cpu.x86.opcodes.stack.x186;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86Stack;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * POPA/POPAD -- Pop all General Registers
 * 
 * Opcode   Instruction   Clocks   Description
 * 61       POPA          24       Pop DI, SI, BP, SP, BX, DX, CX, and AX
 * 61       POPAD         24       Pop EDI, ESI, EBP, ESP, EDX, ECX, and EAX
 * </pre>
 * @author ldaniels
 */
public class POPA extends AbstractOpCode {
	
	/**
	 * Creates a new POPA instruction
	 * @param operand the given {@link Operand operand}
	 */
	public POPA() {
		super();
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// get the stack instance
		final X86Stack stack = cpu.getStack();
		
		// push the operand onto the stack
		stack.pop( cpu.AX );
		stack.pop( cpu.CX );
		stack.pop( cpu.DX );
		stack.pop( cpu.BX );
		stack.pop( cpu.SP );
		stack.pop( cpu.BP );
		stack.pop( cpu.SI );
		stack.pop( cpu.DI );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "POPA" );
	}

}