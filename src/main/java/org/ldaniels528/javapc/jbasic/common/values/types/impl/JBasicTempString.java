package org.ldaniels528.javapc.jbasic.common.values.types.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * JBasic Non-persistent (Temporary) String
 * @author lawrence.daniels@gmail.com
 */
public class JBasicTempString implements StringMemoryObject {
	public static final int MAX_LENGTH	 = 255;
	public static final int ALLOCATE_SIZE = MAX_LENGTH + 1;
	private final StringBuilder buffer;

	/**
	 * Default Constructor
	 */
	public JBasicTempString() {
		this.buffer = new StringBuilder( 255 );
	}
	
	/**
	 * Creates a new GwBasic [Temporary] String
	 * @param string the string that will reside in this object 
	 */
	public JBasicTempString( String string ) {
		this();
		buffer.append( string );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicString#append(org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject)
	 */
	public void append( MemoryObject object ) {
		buffer.append( object.getContent() );		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#destroy()
	 */
	public void destroy() {
		// do nothing
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicString#length()
	 */
	public int length() {
		return buffer.length();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicString#setString(java.lang.String)
	 */
	public void setString( final String string ) {
		buffer.replace( 0, buffer.length(), string );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#duplicate()
	 */
	public MemoryObject duplicate() {		
		JBasicTempString string = new JBasicTempString();
		string.setString( buffer.toString() );
		return string;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#getContent()
	 */
	public Object getContent() {
		return buffer.toString();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#setContent(java.lang.Object)
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
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#getOffset()
	 */
	public int getOffset() {
		return 0;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#getSegment()
	 */
	public int getSegment() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.common.values.types.StringMemoryObject#initialize()
	 */
	public void initialize() {
		buffer.delete( 0, buffer.length() );		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#isCompatibleWith(org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject)
	 */
	public boolean isCompatibleWith(MemoryObject object) {
		return object.isString();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#isNumeric()
	 */
	public boolean isNumeric() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#isString()
	 */
	public boolean isString() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#size()
	 */
	public int size() {
		return ALLOCATE_SIZE;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#toDecimal()
	 */
	public double toDoublePrecision() throws TypeMismatchException {		
		final String svalue = toString();
		return GwBasicValues.parseDecimalString( svalue );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#toDecimal()
	 */
	public float toSinglePrecision() throws TypeMismatchException {		
		final String svalue = toString();
		return (float)GwBasicValues.parseDecimalString( svalue );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#toInteger()
	 */
	public int toInteger() throws TypeMismatchException {		
		final String svalue = toString();
		return GwBasicValues.parseIntegerString( svalue );
	}

	/* 
	 * (non-Javadoc)
	 * @see Comparator#compareTo(Object)
	 */
	public int compareTo(Object o) {
		// if the incoming object is not a string, reject it
		if( !( o instanceof StringMemoryObject ) ) return -1;
		
		// cast the object to a string
		final StringMemoryObject that = (StringMemoryObject)o;
		
		// return the result
		return this.toString().compareTo( that.toString() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return buffer.toString();
	}

}
