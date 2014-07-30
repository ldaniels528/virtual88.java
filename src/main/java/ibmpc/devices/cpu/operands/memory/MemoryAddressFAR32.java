package ibmpc.devices.cpu.operands.memory;

import static ibmpc.devices.memory.X86MemoryUtil.computePhysicalAddress;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Represents a FAR pointer to a 32-bit (double-word) memory address
 * @author lawrence.daniels@gmail.com
 */
public class MemoryAddressFAR32 implements OperandAddress {
	private final IbmPcRandomAccessMemory memory;
	private final int physicalAddress;
	private final int segment;
	private final int offset;
	
	/**
	 * Creates a FAR pointer to a 32-bit memory address
	 * @param memory the given {@link IbmPcRandomAccessMemory memory} instance
	 * @param offset the offset portion of the address
	 */
	public MemoryAddressFAR32( final IbmPcRandomAccessMemory memory, final int segment, final int offset ) {
		this.memory				= memory;
		this.segment			= segment;
		this.offset 			= offset;
		this.physicalAddress	= computePhysicalAddress( segment, offset );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( final Object o ) {
		// the object must be a 32-bit memory address
		if( o instanceof MemoryAddressFAR32 ) {
			// and the physical addresses must match
			final MemoryAddressFAR32 address = (MemoryAddressFAR32)o;
			return address.physicalAddress == this.physicalAddress;
		}
		return false;		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return physicalAddress;
	}
	
	/**
	 * Returns the physical address for the referenced
	 * segment and offset
	 * @return he physical address for the referenced
	 * segment and offset
	 */
	public int getPhysicalAddress() {
		return physicalAddress;
	}
	
	/**
	 * @return the segment
	 */
	public int getSegment() {
		return segment;
	}
	
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#get()
	 */
	public int get() {
		return memory.getDoubleWord( segment, offset );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#set(int)
	 */
	public void set( final int value ) {
		memory.setDoubleWord( segment, offset, value );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.Operand#size()
	 */
	public int size() {
		return SIZE_32BIT;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "%04X:%04X", segment, offset );
	}
	
}
