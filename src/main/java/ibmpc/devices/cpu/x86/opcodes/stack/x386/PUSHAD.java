package ibmpc.devices.cpu.x86.opcodes.stack.x386;

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
public class PUSHAD extends AbstractOpCode {
	
	/**
	 * Creates a new PUSHAD instruction
	 * @param operand the given {@link Operand operand}
	 */
	public PUSHAD() {
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
		stack.push( cpu.EAX );
		stack.push( cpu.ECX );
		stack.push( cpu.EDX );
		stack.push( cpu.EBX );
		stack.push( cpu.ESP );
		stack.push( cpu.EBP );
		stack.push( cpu.ESI );
		stack.push( cpu.EDI );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "PUSHAD" );
	}

}