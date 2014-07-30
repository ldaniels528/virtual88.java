package ibmpc.devices.cpu.x86.opcodes.addressing;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.memory.MemoryReference;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;

/**
 * <pre>
 * Load Executive Address (LEA)
 * 
 * Usage: LEA dest,src
 * Modifies Flags: None
 * 
 * Transfers offset address of "src" to the destination register.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class LEA extends AbstractOpCode {
	private final X86Register dest;
	private final MemoryReference src;
	
	/**
	 * LEA dest, src (e.g. 'LEA AX,[BX+SI]')
	 * @param dest the given {@link X86Register destination}
	 * @param src the given {@link MemoryReference source}
	 */
	public LEA( final X86Register dest, final MemoryReference src ) {
		this.dest	= dest;
		this.src	= src;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		dest.set( src.getOffset() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "LEA %s,%s", dest, src );
	}

}
