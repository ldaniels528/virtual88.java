package org.ldaniels528.javapc.jbasic.common.values.types.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

/**
 * Generic GWBASIC-compatible data object
 * @author lawrence.daniels@gmail.com
 */
public abstract class JBasicObject implements MemoryObject {
	protected final IbmPcRandomAccessMemory memory;
	protected final MemoryManager memoryManager;
	protected int segment; 
	protected int offset;	
	
	//////////////////////////////////////////////////////
	//    Constructor(s)
	//////////////////////////////////////////////////////
	
	/** 
	 * Creates an instance of this value
	 * @param memoryManager the given {@link MemoryManager memory manager}
	 * @param address the physical address of this value in memory
	 */
	public JBasicObject( MemoryManager memoryManager, int segment, int offset ) {		
		this.memory			= memoryManager.getMemory();
		this.memoryManager	= memoryManager;
		this.segment		= segment;
		this.offset			= offset;
	}
	
	//////////////////////////////////////////////////////
	//    Service Method(s)
	//////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#destroy()
	 */
	public void destroy() {
		memoryManager.deallocate( offset, size() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#getOffset()
	 */
	public int getOffset() {
		return offset;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#getSegment()
	 */
	public int getSegment() {
		return segment;
	}
	
}
