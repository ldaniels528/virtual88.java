package org.ldaniels528.javapc.ibmpc.devices.memory;

import org.ldaniels528.javapc.msdos.storage.MsDosDisketteFileControlBlock;

/**
 * Tracks allocation and Deallocation of Random Access Memory (RAM)
 * @see org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory
 * @author lawrence.daniels@gmail.com
 */
public interface MemoryManager {
	
	/**
	 * Allocates a block of memory of the given size 
	 * @param requiredSize the given required block size
	 * @return the offset of the allocated block
	 * @throws OutOfMemoryException
	 */	
	int allocate( int requiredSize ) throws OutOfMemoryException;
	
	/**
	 * Clears the contents of user memory
	 */
	void clear();
	
	/**
	 * Optimizes the unallocate memory blocks
	 */
	void compactUnallocatedBlocks();
	
	/**
	 * Creates an integer memory location
	 * @return a {@link NumberMemoryObject numeric} instance
	 * @throws OutOfMemoryException 
	 */
	NumberMemoryObject createInteger() throws OutOfMemoryException;
	  
	/**
	 * Creates a double precision floating point memory location
	 * @return a {@link NumberMemoryObject numeric} instance
	 * @throws OutOfMemoryException
	 */
	NumberMemoryObject createDoublePrecisionDecimal() throws OutOfMemoryException;
	  
	/**
	 * Creates a single precision floating memory location
	 * @return a {@link NumberMemoryObject numeric} instance
	 * @throws OutOfMemoryException
	 */
	NumberMemoryObject createSinglePrecisionDecimal() throws OutOfMemoryException;
	  
	/**
	 * Creates a string memory location
	 * @return a {@link StringMemoryObject string} instance
	 * @throws OutOfMemoryException
	 */
	StringMemoryObject createString() throws OutOfMemoryException;
	  
	/**
	 * Creates a file control block memory location
	 * @return a {@link MsDosDisketteFileControlBlock file control block} instance
	 * @throws OutOfMemoryException
	 */
	MsDosDisketteFileControlBlock createFileControlBlock() throws OutOfMemoryException;
	
	/**
	 * Deallocates a block of memory
	 * @param offset the offset in memory of the block that will be deallocated
	 * @param length the amount of memory that will be freed
	 */
	void deallocate( int offset, int length );
	
	/**
	 * @return the random access memory instance.
	 */
	IbmPcRandomAccessMemory getMemory();
	
	/**
	 * @return the amount of memory that is currently unallocated
	 */
	int getUnallocatedAmount();
	
}
