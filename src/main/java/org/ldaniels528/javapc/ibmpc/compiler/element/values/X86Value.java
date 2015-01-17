package org.ldaniels528.javapc.ibmpc.compiler.element.values;

import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a numeric value for use with 8086 instructions
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86Value implements X86DataElement {
	
	/**
	 * Validates whether the given content is an integer constant
	 * @param value the given value
	 * @return true, if the content is a valid number (i.e. <tt>34567</tt>)
	 */
	public static boolean isNumeric( final String value ) {
		return GwBasicValues.isNumericConstant( value );
	}
	
	/**
	 * Validates whether the given content is a string constant
	 * @param value the given value
	 * @return true, if the content is a valid number (i.e. <tt>34567</tt>)
	 */
	public static boolean isString( final String value ) {
		return GwBasicValues.isStringConstant( value );
	}
	
	/**
	 * Parse the given numeric string into an x86 value object
	 * @param value the given numeric string
	 * @return an {@link X86Value x86 value} object
	 */
	public static X86Value parseNumericValue( final String value ) {
		final int numValue = (int)GwBasicValues.parseNumericString( value );
		return ( numValue <= 0xFF ) ? new X86ByteValue( numValue ) : new X86WordValue( numValue );
	}
	
	/**
	 * Parse the given numeric string into an x86 value object
	 * @param value the given numeric string
	 * @return an {@link X86Value x86 value} object
	 * @throws X86AssemblyException 
	 */
	public static X86Value parseValue( final String value ) 
	throws X86AssemblyException {
		if( isNumeric( value ) ) return parseNumericValue( value );
		else if( isString( value ) ) return new X86DataValue( value.substring( 1, value.length() - 1 ) );
		else throw new X86AssemblyException( "Unrecognized value type '" + value + "'" );
	}
	
	/**
	 * Returns the underlying value are a set of bytes
	 * @return an array of bytes
	 */
	public abstract byte[] getBytes();
	
	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.machinecode.encoder.X86DataElement#getSequence()
	 */
	public int getSequence() {
		return 0;
	}
	
	/**
	 * Returns the encapsulated value
	 * @return the {@link Object value}
	 */
	public abstract Object getValue();

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.machinecode.encoder.X86DataElement#isMemoryReference()
	 */
	public boolean isMemoryReference() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.machinecode.encoder.X86DataElement#isRegister()
	 */
	public boolean isRegister() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.machinecode.encoder.X86DataElement#isValue()
	 */
	public boolean isValue() {
		return true;
	}
	
	/**
	 * Indicates whether this value is numeric
	 * @return true, if this value is numeric
	 */
	public abstract boolean isNumeric();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getValue().toString();
	}

}