package org.ldaniels528.javapc.jbasic.common.values.types.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.NeverShouldHappenException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a GWBASIC compatible number designed for transitional
 * computation.  This object was no JBasic physical memory.
 * @author lawrence.daniels@gmail.com
 */
public class JBasicTempNumber implements NumberMemoryObject {	
	public static final int MODE_INTEGER 		= 0;
	public static final int MODE_DOUBLE_PREC	= 1;
	public static final int MODE_SINGLE_PREC	= 2;
	public static final int ALLOCATED_SIZE 	= 8;
	private double value;
	private final int mode;	
	
	/**
	 * Creates an instance of this number having the given initial value
	 * @param value the given initial value
	 */
	public JBasicTempNumber( int value ) {
		this.mode  = MODE_INTEGER;
		this.value = value;
	}
	
	/**
	 * Creates an instance of this number having the given initial value
	 * @param value the given initial value
	 */
	public JBasicTempNumber( float value ) {
		this.mode  = MODE_SINGLE_PREC;
		this.value = value;		
	}
	
	/**
	 * Creates an instance of this number having the given initial value
	 * @param value the given initial value
	 */
	public JBasicTempNumber( double value ) {		
		this.mode  = MODE_DOUBLE_PREC;
		this.value = value;		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#getContent()
	 */
	public Object getContent() {		
		return value;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#setContent(java.lang.Object)
	 */
	public void setContent( final Object o )  {
		// is it a string?
		if( o instanceof String ) {
			// cast the object to a string
			final String strnum = (String)o;
			
			// if the string is not numeric, error ...
			if( !GwBasicValues.isNumericConstant( strnum ) )
				throw new TypeMismatchException( this );
			
			// convert the string to a numeric value
			setValue( GwBasicValues.parseNumericString( strnum ) );
		}
		
		// is it a number?
		else if( o instanceof Number ) { 
			// cast the object to a number
			final Number number = (Number)o;
			
			// set the value
			setValue( number.doubleValue() );		
		}
		
		// reject anything else ...
		else throw new TypeMismatchException( this );		
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
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#setValue(double)
	 */
	public void setValue( double value ) {
		this.value = value;		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#add(org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber)
	 */
	public NumberMemoryObject add( NumberMemoryObject number) {				
		return new JBasicTempNumber( value + number.toDoublePrecision() );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#divide(org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber)
	 */
	public NumberMemoryObject divide( NumberMemoryObject number) {		
		return new JBasicTempNumber( value / number.toDoublePrecision() );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#exponent(org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber)
	 */
	public NumberMemoryObject exponent( NumberMemoryObject number) {		
		return new JBasicTempNumber( Math.pow( value, number.toDoublePrecision() ) );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.common.values.types.MemoryObject#initialize()
	 */
	public void initialize() {
		this.value = 0;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#modulus(org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber)
	 */
	public NumberMemoryObject modulus( NumberMemoryObject number) {				
		return new JBasicTempNumber( value % number.toDoublePrecision() );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#multiply(org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber)
	 */
	public NumberMemoryObject multiply( NumberMemoryObject number) {
		JBasicTempNumber object = new JBasicTempNumber( value * number.toDoublePrecision() );		
		return object;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber#subtract(org.ldaniels528.javapc.jbasic.base.values.types.JBasicNumber)
	 */
	public NumberMemoryObject subtract( NumberMemoryObject number) {
		return new JBasicTempNumber( value - number.toDoublePrecision() );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#duplicate()
	 */
	public MemoryObject duplicate() {
		switch( mode ) {
			case MODE_INTEGER: 		return new JBasicTempNumber( (int)value );
			case MODE_SINGLE_PREC: 	return new JBasicTempNumber( (float)value );
			case MODE_DOUBLE_PREC:	return new JBasicTempNumber( (double)value );
			default:		
				throw new NeverShouldHappenException();
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#getOffset()
	 */
	public int getOffset() {
		return -1;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#getSegment()
	 */
	public int getSegment() {
		return -1;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#isCompatibleWith(org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject)
	 */
	public boolean isCompatibleWith(MemoryObject object) {
		return object.isNumeric();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#isNumeric()
	 */
	public boolean isNumeric() {
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#isString()
	 */
	public boolean isString() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#size()
	 */
	public int size() {
		return ALLOCATED_SIZE;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.program.values.types.IbmPcObject#toDoublePrecision()
	 */
	public double toDoublePrecision() {
		return value;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.program.values.types.IbmPcObject#toSinglePrecision()
	 */
	public float toSinglePrecision() {
		return (float)value;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.types.JBasicObject#toInteger()
	 */
	public int toInteger() {
		return (short)value;
	}

	/* 
	 * (non-Javadoc)
	 * @see Comparator#compareTo(Object)
	 */
	public int compareTo(Object o) {
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
	 * @see Object#toString()
	 */
	public String toString() {		
		final double number;
		switch( mode ) {
			case MODE_INTEGER: 		number = toInteger(); break;
			case MODE_DOUBLE_PREC:	number = toDoublePrecision(); break;
			case MODE_SINGLE_PREC: 	number = toSinglePrecision(); break;
			default:					number = toDoublePrecision(); break;
		}		
		return ( number == (int)number )  
				? String.valueOf( (int)number )
				: String.valueOf( number );
	}

}
