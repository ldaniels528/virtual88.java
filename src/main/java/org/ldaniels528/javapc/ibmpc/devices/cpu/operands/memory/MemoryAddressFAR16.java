package org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;

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
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param offset the offset portion of the address
	 */
	public MemoryAddressFAR16( final Intel8086 cpu, final int segment, final int offset ) {
		this.memory	= cpu.getRandomAccessMemory();
		this.segment= segment;
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int get() {
		return memory.getWord( segment, offset );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set( final int value ) {
		memory.setWord( segment, offset, value );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return SIZE_16BIT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( "%04X:%04X", segment, offset );
	}
	
}
