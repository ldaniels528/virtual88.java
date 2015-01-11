package org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory;

import static org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryUtil.computePhysicalAddress;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals( final Object o ) {
		// the object must be a 32-bit memory address
		if( o instanceof MemoryAddressFAR32 ) {
			// and the physical addresses must match
			final MemoryAddressFAR32 address = (MemoryAddressFAR32)o;
			return address.physicalAddress == this.physicalAddress;
		}
		return false;		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int get() {
		return memory.getDoubleWord( segment, offset );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set( final int value ) {
		memory.setDoubleWord( segment, offset, value );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return SIZE_32BIT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( "%04X:%04X", segment, offset );
	}
	
}
