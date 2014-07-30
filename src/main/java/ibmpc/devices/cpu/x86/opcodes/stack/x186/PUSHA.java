package ibmpc.devices.cpu.x86.opcodes.stack.x186;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86Stack;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * PUSHA/PUSHAD -- Push all General Registers
 * 
 * Opcode  Instruction  Clocks   Description
 * 60      PUSHA        18       Push AX, CX, DX, BX, original SP, BP, SI, and DI
 * 60      PUSHAD       18       Push EAX, ECX, EDX, EBX, original ESP, EBP, ESI, and EDI
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class PUSHA extends AbstractOpCode {
	
	/**
	 * Creates a new PUSHA instruction
	 * @param operand the given {@link Operand operand}
	 */
	public PUSHA() {
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
		stack.push( cpu.AX );
		stack.push( cpu.CX );
		stack.push( cpu.DX );
		stack.push( cpu.BX );
		stack.push( cpu.SP );
		stack.push( cpu.BP );
		stack.push( cpu.SI );
		stack.push( cpu.DI );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "PUSHA" );
	}

}