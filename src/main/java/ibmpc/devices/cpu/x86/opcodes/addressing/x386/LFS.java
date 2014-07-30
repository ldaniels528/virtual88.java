package ibmpc.devices.cpu.x86.opcodes.addressing.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.memory.MemoryReference;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * <pre>
 * LFS - Load Pointer Using FS (386+)
 * 
 *	Usage:  LFS     dest,src
 *	Modifies flags: None
 *
 *	Loads 32-bit pointer from memory source to destination register
 *	and FS.  The offset is placed in the destination register and the
 *	segment is placed in FS.  To use this instruction the word at the
 *	lower memory address must contain the offset and the word at the
 *	higher address must contain the segment.  This simplifies the loading
 *	of far pointers from the stack and the interrupt vector table.
 *
 *                           Clocks                 Size
 *	Operands         808x  286   386   486          Bytes
 *	reg,mem           -     -     7     6            5-7
 *	reg,mem (PM)      -     -     22    12           5-7
 * </pre>
 * @author ldaniels
 */
public class LFS extends AbstractOpCode {
	private final X86Register dest;
	private final MemoryReference src;
	
	/**
	 * LFS dst, src (e.g. 'LFS EAX,[BX+SI]')
	 * @param dest the given {@link X86Register destination}
	 * @param src the given {@link MemoryReference source}
	 */
	public LFS( final X86Register dest, final MemoryReference src ) {
		this.dest	= dest;
		this.src	= src;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the random access memory (RAM) instance
		final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();
		
		// cache the segment register (FS)
		final X86Register segReg = cpu.FS;
		
		// get segment and offset
		final int base		= src.getOffset();
		final int offset	= memory.getWord( segReg.get(), base );
		final int segment	= memory.getWord( segReg.get(), base + 2 );
		
		// load the segment and offset into DS:dst
		segReg.set( segment );
		dest.set( offset );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "LFS %s,%s", dest, src );
	}

}
