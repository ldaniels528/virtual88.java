package jbasic.common.values.types.impl;

import ibmpc.devices.memory.MemoryManager;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import java.nio.ByteBuffer;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NeverShouldHappenException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a GWBASIC compatible numeric value
 * @author lawrence.daniels@gmail.com
 */
public class JBasicNumber extends JBasicObject implements NumberMemoryObject {
	public static final int MODE_INTEGER 		= 0;
	public static final int MODE_DOUBLE_PREC	= 1;
	public static final int MODE_SINGLE_PREC	= 2;
	public static final int ALLOCATE_SIZE 		= 8;
	private final ByteBuffer buffer;
	private int mode;

	//////////////////////////////////////////////////////
	//    Constructor(s)
	//////////////////////////////////////////////////////
	
	/** 
	 * Creates an instance of this number
	 * @param memoryManager the given {@link MemoryManager memory manager}
	 * @param segment the physical segment of this number in memory
	 * @param offset the physical offset of this number in memory	
	 */
	public JBasicNumber( MemoryManager memoryManager, int segment, int offset, int mode ) {
		super( memoryManager, segment, offset );
		this.buffer = ByteBuffer.wrap( new byte[ ALLOCATE_SIZE ] );
		this.mode = mode;
	}
	
	//////////////////////////////////////////////////////
	//    Service Method(s)
	//////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see Comparator#compareTo(Object)
	 */
	public int compareTo( Object o ) {
		// if the incoming object is not a string, reject it
		if( !( o instanceof NumberMemoryObject ) ) return -1;
		
		// cast the object to an integer
		final NumberMemoryObject that = (NumberMemoryObject)o;
		
		// get the decimal values of a and b
		final double a = this.toDoublePrecision();
		final double b = that.toDoublePrecision();
		
		// return the result
		return ( a > b ) ? 1 : ( ( a < b ) ? -1 : 0 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.types.JBasicObject#duplicate()
	 */
	public MemoryObject duplicate() {
		switch( mode ) {
			case MODE_INTEGER: 		return new JBasicTempNumber( readInteger() ); 
			case MODE_DOUBLE_PREC:	return new JBasicTempNumber( readDoublePrecision() ); 
			case MODE_SINGLE_PREC: 	return new JBasicTempNumber( readSinglePrecision() ); 
			default:
				throw new NeverShouldHappenException();
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.types.JBasicObject#getContent()
	 */
	public Object getContent() {
		switch( mode ) {
			case MODE_INTEGER: 		return readInteger();
			case MODE_DOUBLE_PREC: 	return readDoublePrecision();
			case MODE_SINGLE_PREC: 	return readSinglePrecision();
			default:					return null;
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#setObject(java.lang.Object)
	 */
	public void setContent( final Object o ) {
		// is it a number?
		if( o instanceof Number ) { 
			// cast the object to a number
			final Number number = (Number)o;
			
			// set the value
			setValue( number.doubleValue() );		
		}
		
		// is it a string?
		else if( o instanceof String ) {
			// cast the object to a string
			final String strnum = (String)o;
			
			// if the string is not numeric, error ...
			if( !GwBasicValues.isNumericConstant( strnum ) )
				throw new TypeMismatchException( this );
			
			// convert the string to a numeric value
			setValue( GwBasicValues.parseNumericString( strnum ) );
		}		
		
		// reject anything else ...
		else throw new NeverShouldHappenException();	
	}
	
	/**
	 * Sets the contents of the given integer into memory
	 * @param value the given integer value
	 * @throws JBasicException 
	 */
	public void setValue( double value ) {	
		switch( mode ) {
			case MODE_INTEGER: 		writeInteger( value ); break;
			case MODE_DOUBLE_PREC:	writeDoublePrecision( value ); break;
			case MODE_SINGLE_PREC:	writeSinglePrecision( value ); break;
		}
	}
	
	//////////////////////////////////////////////////////
	//    Type Identification Method(s)
	//////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see jbasic.common.values.types.MemoryObject#initialize()
	 */
	public void initialize() {
		
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#isCompatibleWith(jbasic.values.JBasicObject)
	 */
	public boolean isCompatibleWith( MemoryObject object ) {
		return object.isNumeric();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#isNumeric()
	 */
	public boolean isNumeric() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#isString()
	 */
	public boolean isString() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#size()
	 */
	public int size() {		
		return ALLOCATE_SIZE;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#toInteger()
	 */
	public int toInteger() {
		Number number = (Number)getContent();
		return number.intValue();
	}
	
	/**
	 * @return a double precision decimal value
	 */
	public double toDoublePrecision() {
		Number number = (Number)getContent();
		return number.doubleValue();
	}
	
	/**
	 * @return a single precision decimal value
	 */
	public float toSinglePrecision() {
		Number number = (Number)getContent();
		return number.floatValue();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicObject#toString()
	 */
	public String toString() {
		final double number;
		switch( mode ) {
			case MODE_INTEGER: 		number = readInteger(); break;
			case MODE_DOUBLE_PREC:	number = readDoublePrecision(); break;
			case MODE_SINGLE_PREC: 	number = readSinglePrecision(); break;
			default:					number = -1; break;
		}		
		return ( number == (int)number )  
				? String.valueOf( (int)number )
				: String.valueOf( number );
	}
	
	//////////////////////////////////////////////////////
	//    Type Identification Method(s)
	//////////////////////////////////////////////////////
	
	/** 
	 * @return true, if this number is an integer value
	 */
	public boolean isInteger() {
		return mode == MODE_INTEGER;
	}
	
	/** 
	 * @return true, if this number is a single precision value
	 */
	public boolean isSinglePrecision() {
		return mode == MODE_SINGLE_PREC;
	}
	/** 
	 * @return true, if this number is a double precision value
	 */
	public boolean isDoublePrecision() {
		return mode == MODE_DOUBLE_PREC;
	}
	
	//////////////////////////////////////////////////////
	//    Mathematical Method(s)
	//////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicNumber#add(JBasicNumber)
	 */
	public NumberMemoryObject add( NumberMemoryObject number ) {
		NumberMemoryObject returnVal = (NumberMemoryObject)this.duplicate();
		returnVal.setValue( this.toDoublePrecision() + number.toDoublePrecision() );
		return returnVal;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.types.JBasicNumber#divide(jbasic.values.types.JBasicNumber)
	 */
	public NumberMemoryObject divide( NumberMemoryObject number ) {
		NumberMemoryObject returnVal = (NumberMemoryObject)this.duplicate();
		returnVal.setValue( this.toDoublePrecision() / number.toDoublePrecision() );
		return returnVal;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicNumber#exponent(jbasic.values.JBasicNumber)
	 */
	public NumberMemoryObject exponent( NumberMemoryObject number ) {
		NumberMemoryObject returnVal = (NumberMemoryObject)this.duplicate();
		returnVal.setValue( (float)Math.pow( this.toDoublePrecision(), number.toDoublePrecision() ) );
		return returnVal;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.JBasicNumber#modulus(jbasic.values.JBasicNumber)
	 */
	public NumberMemoryObject modulus( NumberMemoryObject number ) {
		NumberMemoryObject returnVal = (NumberMemoryObject)this.duplicate();
		returnVal.setValue( this.toDoublePrecision() % number.toDoublePrecision() );
		return returnVal;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.types.JBasicNumber#multiply(jbasic.values.types.JBasicNumber)
	 */
	public NumberMemoryObject multiply( NumberMemoryObject number ) {
		NumberMemoryObject returnVal = (NumberMemoryObject)this.duplicate();
		returnVal.setValue( this.toDoublePrecision() * number.toDoublePrecision() );
		return returnVal;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.types.JBasicNumber#subtract(jbasic.values.types.JBasicNumber)
	 */
	public NumberMemoryObject subtract( NumberMemoryObject number ) {
		NumberMemoryObject returnVal = (NumberMemoryObject)this.duplicate();
		returnVal.setValue( this.toDoublePrecision() - number.toDoublePrecision() );
		return returnVal;
	}
	
	//////////////////////////////////////////////////////
	//    Data Persistence and Retrieval Method(s)
	//////////////////////////////////////////////////////
	
	/**
	 * @return an integer value
	 */
	private int readInteger() {
		return memory.getWord( segment, offset );
	}
	
	/**
	 * Writes an integer value to memory
	 * @param value the given integer value
	 */
	private void writeInteger( double value ) {
		memory.setWord( segment, offset, (int)value );
	}
	
	/**
	 * @return a double precision decimal value
	 */
	private double readDoublePrecision() {
		// get the block containing our data
		final byte[] tempdata = memory.getBytes( segment, offset, ALLOCATE_SIZE );
		
		// extract and return a float
		final ByteBuffer buffer = ByteBuffer.wrap( tempdata );
	    return buffer.getDouble();
	}
	
	/**
	 * Writes a double precision value to memory
	 * @param value the given double precision value
	 */
	private void writeDoublePrecision( double value ) {
		// put the double in the buffer
		buffer.rewind();
		buffer.putDouble( value );
		
		// write the contents to memory
		memory.setBytes( segment, offset, buffer.array(), ALLOCATE_SIZE );
	}
	
	/**
	 * @return a single precision decimal value
	 */
	private double readSinglePrecision() {
		// get the block containing our data
		final byte[] tempdata = memory.getBytes( segment, offset, ALLOCATE_SIZE );
		
		// extract and return a float
		final ByteBuffer buffer = ByteBuffer.wrap( tempdata );
	    return buffer.getFloat();
	}
	
	/**
	 * Writes a single precision value to memory
	 * @param value the given single precision value
	 */
	private void writeSinglePrecision( double value ) {
		// put the double in the buffer
		buffer.rewind();
		buffer.putFloat( (float)value );
		
		// write the contents to memory
		memory.setBytes( segment, offset, buffer.array(), ALLOCATE_SIZE );
	}
	
}
