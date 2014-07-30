package ibmpc.devices.cpu.operands.memory;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Represents a NEAR pointer to a 16-bit (word) memory address
 * @author lawrence.daniels@gmail.com
 */
public class MemoryAddressNEAR16 implements OperandAddress {
	private final IbmPcRandomAccessMemory memory;
	private final Intel80x86 cpu;
	private final int offset;
	
	/**
	 * Creates a NEAR pointer to a 16-bit memory address
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @param offset the offset portion of the address
	 */
	public MemoryAddressNEAR16( final Intel80x86 cpu, final int offset ) {
		this.cpu	= cpu;
		this.memory	= cpu.getRandomAccessMemory();
		this.offset = offset;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#get()
	 */
	public int get() {
		return memory.getWord( cpu.DS.get(), offset );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#set(int)
	 */
	public void set( final int value ) {
		memory.setWord( cpu.DS.get(), offset, value );
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
		return String.format( "[%04X]", offset );
	}
	
}
