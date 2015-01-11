package org.ldaniels528.javapc.jbasic.common.values.types.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.OutOfMemoryException;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.StringTooLongException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a GWBASIC String
 * @author lawrence.daniels@gmail.com
 */
public class JBasicString extends JBasicObject implements StringMemoryObject {	
	public static final int MAX_LENGTH	= 255;
	public static final int POINTER_LENGTH	= 2;
	public static final int ALLOCATE_SIZE 	= POINTER_LENGTH + 1;
	private static final String EMPTY		= "";
	
	//////////////////////////////////////////////////////
	//    Constructor(s)
	//////////////////////////////////////////////////////
	
	/** 
	 * Creates an instance of this value
	 * @param memoryManager the given {@link MemoryManager memory manager}
	 * @param address the physical address of this value in memory
	 */
	public JBasicString( MemoryManager memoryManager, int segment, int offset ) {
		super( memoryManager, segment, offset );		
	}
	
	//////////////////////////////////////////////////////
	//    Service Method(s)
	//////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicString#append(org.ldaniels528.javapc.jbasic.values.JBasicObject)
	 */
	public void append( MemoryObject object ) {
		// build a copy of the new string
		StringBuilder buffer = new StringBuilder( MAX_LENGTH );
		buffer.append( this.toString() );
		buffer.append( object.toString() );
		
		// update this object with the new string data
		replaceString( buffer.toString() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see Comparator#compareTo(Object)
	 */
	public int compareTo( Object o ) {
		// if the incoming object is not a string, reject it
		if( !( o instanceof StringMemoryObject ) ) return -1;
		
		// cast the object to a string
		final StringMemoryObject that = (StringMemoryObject)o;
		
		// return the result
		return this.toString().compareTo( that.toString() );		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#destroy()
	 */
	public void destroy() {		
		// dellocated the string data
		final int ptrOffset = getPointerOffset();
		final int length	= getLength();	
		memoryManager.deallocate( ptrOffset, length );
		
		// allow parent to destroy this string
		super.destroy();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#copy()
	 */
	public MemoryObject duplicate() {
		// create an empty string
		final StringMemoryObject newString = new JBasicTempString();
		
		// put the contents of the source string into the new string				
		newString.setString( this.toString() );
		return newString;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.types.JBasicObject#getContent()
	 */
	public Object getContent() {
		return toString();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.common.values.types.StringMemoryObject#initialize()
	 */
	public void initialize() {
		this.setLength( 0 );
		this.setPointerOffset( 0 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#isCompatibleWith(org.ldaniels528.javapc.jbasic.values.JBasicObject)
	 */
	public boolean isCompatibleWith( MemoryObject object ) {
		return object.isString();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#isNumeric()
	 */
	public boolean isNumeric() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#isString()
	 */
	public boolean isString() {
		return true;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#length()
	 */
	public int length() {
		return getLength();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicString#setString(java.lang.String)
	 */
	public void setString( String string ) {
		// update this object with the new string
		replaceString( string );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#setObject(java.lang.Object)
	 */
	public void setContent( final Object o ) {
		// if it's not a string, reject it
		if( !( o instanceof String ) ) 
			throw new TypeMismatchException( this );
		
		// otherwise, set it
		final String s = (String)o; 
		setString( s );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#size()
	 */
	public int size() {
		return ALLOCATE_SIZE;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#toDouble()
	 */
	public double toDoublePrecision() throws TypeMismatchException {
		final String svalue = toString();
		return GwBasicValues.parseDecimalString( svalue );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicObject#toInteger()
	 */
	public int toInteger() throws TypeMismatchException {
		final String svalue = toString();
		return GwBasicValues.parseIntegerString( svalue );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.program.values.types.IbmPcObject#toSinglePrecision()
	 */
	public float toSinglePrecision() throws TypeMismatchException {
		final String svalue = toString();
		return (float)GwBasicValues.parseDecimalString( svalue );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.JBasicString#toString()
	 */
	public String toString() {
		// get the pointer offset
		final int pointer = getPointerOffset(); 
			
		// if this string has not been allocated, return an empty string
		if( pointer == 0 ) return EMPTY;
		
		// get the length of the string
		final int length = length();
		
		// if the length is zero, return an empty string
		if( length == 0 ) return EMPTY;
		
		// copy the data into the buffer
		final byte[] tempdata = memory.getBytes( segment, pointer, length );
		
		// return the string
		return new String( tempdata );
	}
	
	//////////////////////////////////////////////////////
	//    Internal Service Method(s)
	//////////////////////////////////////////////////////

	/**
	 * Replaces the internal string data with the given string
	 * @param newString the given replacement string
	 * @throws OutOfMemoryException
	 * @throws StringTooLongException
	 */
	private void replaceString( String newString ) 
	throws OutOfMemoryException, StringTooLongException {
		// make sure the new string does not exceed the maximum
		if( newString.length() > MAX_LENGTH )
			throw new StringTooLongException( this );

		// get the current length and pointer offset
		final int oldLength	  = getLength();
		final int oldPtrOffset = getPointerOffset();
				
		// allocate a block for the new string
		final int newLength	  = newString.length();
		final int newPtrOffset = memoryManager.allocate( newLength );
		
		// set the new length and pointer offset of the string 	
		setLength( newLength );
		setPointerOffset( newPtrOffset );
		
		// copy the string into memory
		memory.setBytes( segment, newPtrOffset, newString.getBytes(), newLength );
		
		// deallocate the old string (if allocated)
		if( oldPtrOffset != 0 ) {
			memoryManager.deallocate( oldPtrOffset, oldLength );
		}
	}
	
	/**
	 * @return the length of the string
	 */
	private int getLength() {
		return memory.getByte( segment, offset );
	}
	
	/**
	 * Sets the length byte of the string to the given length
	 * @param length the given length
	 */
	private void setLength( int length ) {
		memory.setByte( segment, offset, (byte)length );
	}
	
	/**
	 * @return the 16-bit offset of the pointer to the actual string data.
	 */
	private int getPointerOffset() {
		return memory.getWord( segment, offset + 1 );
	}
	
	/**
	 * Sets the data pointer for this string
	 * @param ptrOffset the given offset in memory of the actual string data
	 */
	private void setPointerOffset( int ptrOffset ) {
		memory.setWord( segment, offset + 1, ptrOffset );
	}

}
