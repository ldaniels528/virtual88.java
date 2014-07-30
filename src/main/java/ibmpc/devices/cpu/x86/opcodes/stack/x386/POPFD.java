package ibmpc.devices.cpu.x86.opcodes.stack.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86Stack;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.opcodes.stack.POPF;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * POPF/POPFD -- Pop Stack into FLAGS or EFLAGS Register
 * 
 * Description
 * POPF/POPFD pops the word or doubleword on the top of the 
 * stack and stores the value in the flags register. If the 
 * operand-size attribute of the instruction is 16 bits, 
 * then a word is popped and the value is stored in FLAGS. 
 * If the operand-size attribute is 32 bits, then a doubleword 
 * is popped and the value is stored in EFLAGS.
 * 
 * Refer to Chapter 2 and Chapter 4 for information about the 
 * FLAGS and EFLAGS registers. Note that bits 16 and 17 of EFLAGS, 
 * called VM and RF, respectively, are not affected by POPF or POPFD.
 * 
 * The I/O privilege level is altered only when executing at privilege level 0. 
 * The interrupt flag is altered only when executing at a level at least as 
 * privileged as the I/O privilege level. (Real-address mode is equivalent to 
 * privilege level 0.) If a POPF instruction is executed with insufficient 
 * privilege, an exception does not occur, but the privileged bits do not change.
 * 
 * Opcode   Instruction   Clocks   Description
 * 9D       POPF          5        Pop top of stack FLAGS
 * 9D       POPFD         5        Pop top of stack into EFLAGS
 * </pre>
 * @see POPF
 * @author lawrence.daniels@gmail.com
 */
public class POPFD extends AbstractOpCode {
	private static POPFD instance = new POPFD();
	
	/**
	 * Private constructor
	 */
	private POPFD() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static POPFD getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// get the stack instance
		final X86Stack stack = cpu.getStack();
		
		// get the two 16-bit values from the stack
		final int value16Hi 	= stack.popValue();
		final int value16Lo 	= stack.popValue();
		
		// combine the 16-bit values into a single 32-bit value
		final int value32 = ( value16Hi << 16 ) | value16Lo;
		
		// set the flags
		cpu.FLAGS.set( value32 );
	}

}
