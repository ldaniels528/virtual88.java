package org.ldaniels528.javapc.jbasic.common.values.types.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import java.nio.ByteBuffer;

import org.ldaniels528.javapc.msdos.storage.MsDosDisketteFileControlBlock;

import org.ldaniels528.javapc.jbasic.common.exceptions.NeverShouldHappenException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;

/**
 * GWBASIC File Control Block
 * @author lawrence.daniels@gmail.com
 */
public class JBasicFileControlBlock extends JBasicObject 
implements MsDosDisketteFileControlBlock {
	public static final int ALLOCATE_SIZE = 188;
	
	///////////////////////////////////////////////////////
	//      Constructor(s)
	///////////////////////////////////////////////////////

	/**
	 * Creates an instance of this file control block
	 * @param memoryManager the given {@link MemoryManager memory manager}
	 * @param segment the given memory segment
	 * @param offset the given memory offset
	 */
	public JBasicFileControlBlock( MemoryManager memoryManager, 
								  int segment, 
								  int offset ) {
		super( memoryManager, segment, offset );		
	}

	///////////////////////////////////////////////////////
	//      Accessor and Mutator Method(s)
	///////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getBuffer()
	 */
	public byte[] getBuffer() {
		return memory.getBytes( segment, offset + POS_BUFFER, LEN_BUFFER );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setBuffer(byte[])
	 */
	public void setBuffer( byte[] buffer ) {
		memory.setBytes( segment, offset + POS_BUFFER, buffer, LEN_BUFFER );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#getContent()
	 */
	public Object getContent() {
		return new String( getBuffer() );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#setContent(java.lang.Object)
	 */
	public void setContent( final Object content ) {
		setBuffer( content.toString().getBytes() );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getDevice()
	 */
	public int getDevice() {
		return memory.getByte( segment, offset + POS_DEVICE );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setDevice(int)
	 */
	public void setDevice( int device ) {
		memory.setByte( segment, offset + POS_DEVICE, (byte)device );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getDisketteControlBlock()
	 */
	public byte[] getDisketteControlBlock() {
		return memory.getBytes( segment, offset + POS_FCB, LEN_FCB );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setDisketteControlBlock(byte[])
	 */
	public void setDisketteControlBlock( byte[] block ) {
		memory.setBytes( segment, offset + POS_FCB, block, LEN_FCB );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getFlags()
	 */
	public int getFlags() {
		return memory.getByte( segment, offset + POS_FLAGS );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setFlags(int)
	 */
	public void setFlags( int flags ) {
		memory.setByte( segment, offset + POS_FLAGS, (byte)flags );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getLogicalRecordNumber()
	 */
	public int getLogicalRecordNumber() {
		final byte[] block = memory.getBytes( segment, offset + POS_LOGREC, LEN_LOGREC );		
		ByteBuffer buf = ByteBuffer.wrap( block );
		return buf.getShort();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setLogicalRecordNumber(int)
	 */
	public void setLogicalRecordNumber( int recordNumber ) {
		final byte[] block = new byte[ LEN_LOGREC ];
		ByteBuffer buf = ByteBuffer.wrap( block );
		buf.putShort( (short)recordNumber );		
		memory.setBytes( segment, offset + POS_PHYREC, block, LEN_LOGREC );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getMode()
	 */
	public int getMode() {
		return memory.getByte( segment, offset + POS_MODE );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setMode(int)
	 */
	public void setMode( int mode ) {
		memory.setByte( segment, offset + POS_MODE, (byte)mode );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getPhysicalRecordNumber()
	 */
	public int getPhysicalRecordNumber() {
		final byte[] block = memory.getBytes( segment, offset + POS_PHYREC, LEN_PHYREC );		
		ByteBuffer buf = ByteBuffer.wrap( block );
		return buf.getShort();				
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setPhysicalRecordNumber(int)
	 */
	public void setPhysicalRecordNumber( int recordNumber ) {
		final byte[] block = new byte[ LEN_PHYREC ];
		ByteBuffer buf = ByteBuffer.wrap( block );
		buf.putShort( (short)recordNumber );		
		memory.setBytes( segment, offset + POS_PHYREC, block, LEN_PHYREC );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getPosition()
	 */
	public int getPosition() {
		return memory.getByte( segment, offset + POS_POS );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setPosition(int)
	 */
	public void setPosition( int position ) {
		memory.setByte( segment, offset + POS_POS, (byte)position );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getWidth()
	 */
	public int getWidth() {
		return memory.getByte( segment, offset + POS_WIDTH );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setWidth(int)
	 */
	public void setWidth( int width ) {
		memory.setByte( segment, offset + POS_WIDTH, (byte)width );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#getVariableRecordLength()
	 */
	public int getVariableRecordLength() {
		return memory.getByte( segment, offset + POS_VERCL );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicFileControlBlock#setVariableRecordLength(int)
	 */
	public void setVariableRecordLength( int varRecLength ) {
		memory.setByte( segment, offset + POS_VERCL, (byte)varRecLength );
	}
	
	///////////////////////////////////////////////////////
	//      Service Method(s)
	///////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#duplicate()
	 */
	public MemoryObject duplicate() {
		throw new NeverShouldHappenException();
	}
	
	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.common.values.types.MemoryObject#initialize()
	 */
	public void initialize() {
		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#isCompatibleWith(org.ldaniels528.javapc.jbasic.values.types.JBasicObject)
	 */
	public boolean isCompatibleWith(MemoryObject object) {
		return ( object instanceof MsDosDisketteFileControlBlock );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#isNumeric()
	 */
	public boolean isNumeric() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#isString()
	 */
	public boolean isString() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#size()
	 */
	public int size() {
		return LENGTH;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#toDoublePrecision()
	 */
	public double toDoublePrecision() throws TypeMismatchException {
		throw new TypeMismatchException( this );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#toInteger()
	 */
	public int toInteger() throws TypeMismatchException {
		throw new TypeMismatchException( this );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.program.values.types.IbmPcObject#toSinglePrecision()
	 */
	public float toSinglePrecision() throws TypeMismatchException {
		throw new TypeMismatchException( this );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.program.values.types.IbmPcObject#toString()
	 */
	public String toString() {
		return new String( getBuffer() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see Comparator#compareTo(Object)
	 */
	public int compareTo( Object o ) {
		// must be a file control block
		if( !( o instanceof MsDosDisketteFileControlBlock ) ) return -1;
		
		// cast to file control block
		//JBasicFileControlBlock that = (JBasicFileControlBlock)o;						
		return 0;
	}

}
