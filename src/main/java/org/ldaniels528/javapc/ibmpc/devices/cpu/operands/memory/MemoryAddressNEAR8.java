package org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Represents a NEAR pointer to an 8-bit (byte) memory address
 * @author lawrence.daniels@gmail.com
 */
public class MemoryAddressNEAR8 implements OperandAddress {
	private final IbmPcRandomAccessMemory memory;
	private final Intel8086 cpu;
	private final int offset;
	
	/**
	 * Creates a NEAR pointer to an 8-bit memory address
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param offset the offset portion of the address
	 */
	public MemoryAddressNEAR8( final Intel8086 cpu, final int offset ) {
		this.cpu	= cpu;
		this.memory	= cpu.getRandomAccessMemory();
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int get() {
		return memory.getByte( cpu.DS.get(), offset );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set( final int value ) {
		memory.setByte( cpu.DS.get(), offset, (byte)value );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return SIZE_8BIT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( "[%04X]", offset );
	}
	
}
