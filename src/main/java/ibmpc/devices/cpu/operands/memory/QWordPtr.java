package ibmpc.devices.cpu.operands.memory;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Represents a pointer to a 64-bit (quad word) memory address
 * @author lawrence.daniels@gmail.com
 */
public class QWordPtr implements MemoryPointer {
	private final IbmPcRandomAccessMemory memory;
	private final MemoryReference memoryRef;
	private final Intel80x86 cpu;
	
	/**
	 * Creates a pointer to a 64-bit memory address
	 * @param memoryRef the given {@link MemoryReference memory reference}
	 */
	public QWordPtr( final MemoryReference memoryRef ) {
		this.cpu		= memoryRef.getCPU();
		this.memory		= cpu.getRandomAccessMemory();
		this.memoryRef	= memoryRef;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int get() {
		return memory.getDoubleWord( cpu.DS.get(), memoryRef.getOffset() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set( final int value ) {
		// get the offset of the memory reference
		final int offset = memoryRef.getOffset();
		
		// set the value
		memory.setQuadWord( cpu.DS.get(), offset, value );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MemoryReference getMemoryReference() {
		return memoryRef;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return SIZE_64BIT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( "QWORD PTR %s", memoryRef );
	}
	
}
