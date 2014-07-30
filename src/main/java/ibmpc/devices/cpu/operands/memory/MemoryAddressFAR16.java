package ibmpc.devices.cpu.operands.memory;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Represents a FAR pointer to a 16-bit (word) memory address
 * @author lawrence.daniels@gmail.com
 */
public class MemoryAddressFAR16 implements OperandAddress {
	private final IbmPcRandomAccessMemory memory;
	private final int segment;
	private final int offset;
	
	/**
	 * Creates a FAR pointer to a 16-bit memory address
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @param offset the offset portion of the address
	 */
	public MemoryAddressFAR16( final Intel80x86 cpu, final int segment, final int offset ) {
		this.memory	= cpu.getRandomAccessMemory();
		this.segment= segment;
		this.offset = offset;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#get()
	 */
	public int get() {
		return memory.getWord( segment, offset );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#set(int)
	 */
	public void set( final int value ) {
		memory.setWord( segment, offset, value );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#size()
	 */
	public int size() {
		return SIZE_16BIT;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "%04X:%04X", segment, offset );
	}
	
}
