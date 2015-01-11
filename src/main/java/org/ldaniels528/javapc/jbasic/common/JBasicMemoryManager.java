package org.ldaniels528.javapc.jbasic.common;

import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.OutOfMemoryException;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.ldaniels528.javapc.msdos.storage.MsDosDisketteFileControlBlock;

import org.ldaniels528.javapc.jbasic.common.values.Values;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicFileControlBlock;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicNumber;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicString;

/**
 * Tracks allocation and Deallocation of Random Access Memory (RAM)
 * @see org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager
 * @see org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("unchecked")
public class JBasicMemoryManager implements MemoryManager {
	private final MemoryBlockComparator comparator = new MemoryBlockComparator();
	private final IbmPcRandomAccessMemory memory;	  
	private final List<MemoryBlock> blocks;	
	private final int segment;
	private final int size;
	
	//////////////////////////////////////////////////////
	//    Constructor(s)
	//////////////////////////////////////////////////////
	
	/**
	 * Default Constructor
	 */
	public JBasicMemoryManager( final IbmPcRandomAccessMemory memory, final int segment ) {
		this.blocks		= new LinkedList<MemoryBlock>();
		this.memory		= memory;
		this.segment	= segment;
		this.size		= 0x10000;		
		blocks.add( new MemoryBlock( 0, size ) );
	}	
	
	//////////////////////////////////////////////////////
	//    Service Method(s)
	//////////////////////////////////////////////////////
	
	/**
	 * Allocates a block of memory of the given size 
	 * @param requiredSize the given required block size
	 * @return the offset of the allocated block
	 * @throws OutOfMemoryException
	 */	
	public int allocate( final int requiredSize ) throws OutOfMemoryException {
		// optimize memory
		compactUnallocatedBlocks();
		
		// iterate the set of objects
		for( int n = 0; n < blocks.size(); n++ ) {
			final MemoryBlock block = (MemoryBlock)blocks.get( n );
			
			// if the block is large enough, use it...
			if( block.length() >= requiredSize ) {
				// remove the larger block
				blocks.remove( block );
				
				// if the block was actually larger than what we needed,
				// take what we need, and re-add a smaller block
				if( block.length() > requiredSize ) {
					// get the new offset and size
					final int newOffset = block.offset() + requiredSize;
					final int newSize = block.length() - requiredSize;
					
					// create a new block
					final MemoryBlock newBlock = new MemoryBlock( newOffset, newSize );
					blocks.add( newBlock );
				}
				
				// return the offset
				return block.offset();
			}
		}
		
		throw new OutOfMemoryException( null );
	}	
	
	/**
	 * Clears the contents of user memory
	 */
	public void clear() {
		blocks.clear();
		blocks.add( new MemoryBlock( 0, size ) );
	}
	
	/**
	 * Optimizes the unallocate memory blocks
	 */
	public void compactUnallocatedBlocks() {
		// sort the objects
		Collections.sort( blocks, comparator );
		
		// iterate the list looking for blocks to combine
		for( int n = 1; n < blocks.size(); n++ ) {
			// get the previous block
			final MemoryBlock block1 = (MemoryBlock)blocks.get( n-1 );
			
			// get the current block
			final MemoryBlock block2 = (MemoryBlock)blocks.get( n );						
			
			// if the offset of the current block is equal to the
			// offset + length of the previous block, combine them			
			if( block2.offset() == block1.offset() + block1.length() ) {
				// remove the old blocks
				blocks.remove( block1 );				
				blocks.remove( block2 );
				
				// add a new combined block
				final MemoryBlock combinedBlock = new MemoryBlock( block1.offset(), block1.length() + block2.length() );						
				blocks.add( combinedBlock );
			}
		}	
	}
	
	  /**
	   * Creates an integer memory location
	   * @return a {@link NumberMemoryObject numeric} instance
	   * @throws OutOfMemoryException 
	   */
	  public NumberMemoryObject createInteger() throws OutOfMemoryException {
		  final int offset = allocate( JBasicNumber.ALLOCATE_SIZE );
		  final NumberMemoryObject number = new JBasicNumber( this, segment, offset, JBasicNumber.MODE_INTEGER );
		  return number;
	  }
	  
	  /**
	   * Creates a double precision floating point memory location
	   * @return a {@link NumberMemoryObject numeric} instance
	   * @throws OutOfMemoryException
	   */
	  public NumberMemoryObject createDoublePrecisionDecimal() throws OutOfMemoryException {
		  final int offset = allocate( JBasicNumber.ALLOCATE_SIZE );
		  final NumberMemoryObject number = new JBasicNumber( this, segment, offset, JBasicNumber.MODE_DOUBLE_PREC );
		  return number;
	  }
	  
	  /**
	   * Creates a single precision floating memory location
	   * @return a {@link NumberMemoryObject numeric} instance
	   * @throws OutOfMemoryException
	   */
	  public NumberMemoryObject createSinglePrecisionDecimal() throws OutOfMemoryException {
		  final int offset = allocate( JBasicNumber.ALLOCATE_SIZE );
		  final NumberMemoryObject number = new JBasicNumber( this, segment, offset, JBasicNumber.MODE_SINGLE_PREC );		  
		  return number;
	  }
	  
	  /**
	   * Creates a string memory location
	   * @return a {@link StringMemoryObject string} instance
	   * @throws OutOfMemoryException
	   */
	  public StringMemoryObject createString() throws OutOfMemoryException {
		  final int offset = allocate( JBasicString.ALLOCATE_SIZE );
		  final StringMemoryObject string = new JBasicString( this, segment, offset );
		  string.initialize();
		  return string;
	  }
	  
	  /**
	   * Creates a file control block memory location
	   * @return a {@link MsDosDisketteFileControlBlock file control block} instance
	   * @throws OutOfMemoryException
	   */
	  public MsDosDisketteFileControlBlock createFileControlBlock() throws OutOfMemoryException {
		  final int offset = allocate( JBasicFileControlBlock.ALLOCATE_SIZE );
		  final MsDosDisketteFileControlBlock fcb = new JBasicFileControlBlock( this, segment, offset );		  
		  return fcb;
	  }	  	  
	
	/**
	 * Deallocates a block of memory
	 * @param offset the offset in memory of the block that will be deallocated
	 * @param length the amount of memory that will be freed
	 */
	public void deallocate( final int offset, final int length ) {
		// construct a free memory block for this object 
		MemoryBlock block = new MemoryBlock( offset, length );
		
		// add the block
		blocks.add( block );
	}
	
	/**
	 * @return the random access memory instance.
	 */
	public IbmPcRandomAccessMemory getMemory() {
		return memory;
	}
	
	/**
	 * @return the amount of memory that is currently unallocated
	 */
	public int getUnallocatedAmount() {
		// compact unallocated blocks
		compactUnallocatedBlocks();
		
		// calculate the amount of unallocated memory
		int unallocated = 0;
		for( MemoryBlock block : blocks ) {
			unallocated += block.length();
		}
		return unallocated;
	}
	
	//////////////////////////////////////////////////////
	//    Memory Block Inner Class
	//////////////////////////////////////////////////////
	
	/**
	 * Represents a block of free memory
	 * @author lawrence.daniels@gmail.com	
	 */
	private class MemoryBlock {
		private final int offset;
		private final int length;
		
		/**
		 * Creates a memory block instance
		 * @param offset the offset of the memory block
		 * @param length the length of the memory block
		 */
		public MemoryBlock( final int offset, final int length ) {
			this.offset = offset;
			this.length = length;
		}
		
		/** 
		 * @return the offset of the memory block
		 */
		public int offset() {
			return offset;
		}
		
		/** 
		 * @return the offset of the memory block
		 */
		public int length() {
			return length;
		}		
		
		/* 
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "block@" + Values.toHexadecimal( offset ) + 
					"[" + offset + ".." + ( offset + length - 1 ) + "]";
		}
		
	}
	
	///////////////////////////////////////////////////////
	//      Memory Block Comparator Inner Class
	///////////////////////////////////////////////////////
	
	/**
	 * Memory Block Comparator
	 */
	private static class MemoryBlockComparator implements Comparator {

		/* 
		 * (non-javadoc)
		 * @see Comparator#compare(java.lang.Object,java.lang.Object)
		 */
		public int compare( Object obj1, Object obj2 ) {
			// if either object is not a memory block, error...
			if( !( obj1 instanceof MemoryBlock ) ||
				!( obj2 instanceof MemoryBlock ) ) 
				throw new ClassCastException();
				
			// cast them to memory blocks
			final MemoryBlock block1 = (MemoryBlock)obj1;
			final MemoryBlock block2 = (MemoryBlock)obj2;
				
			// sort the memory blocks in ascending order by offset 
			if( block1.offset() < block2.offset() ) return -1;
			else if( block1.offset() > block2.offset() ) return 1;				
			else return 0;
		}		
	}	

}
