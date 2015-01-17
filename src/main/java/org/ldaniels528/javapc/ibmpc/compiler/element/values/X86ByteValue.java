package org.ldaniels528.javapc.ibmpc.compiler.element.values;

import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a 8-bit value
 * @author lawrence.daniels@gmail.com
 */
public class X86ByteValue extends X86NumericValue {
	// define a byte value of 1
	public static final X86ByteValue ONE = new X86ByteValue( 1 );
	
	/**
	 * Creates a new 8-bit 8086 value
	 * @param value the given 8-bit value
	 */
	public X86ByteValue( final int value ) {
		super( value & 0x00FF ); // mask off upper portion
	}
	
	/**
	 * Creates a new 8-bit 8086 value
	 * @param value the given 8-bit value
	 */
	public X86ByteValue( final String value ) {
		this( (int)GwBasicValues.parseNumericString( value ) );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.machinecode.encoder.values.X86Value#getBytes()
	 */
	public byte[] getBytes() {
		return new byte[] { (byte)value };
	}
	
	/**
	 * Indicates whether the represented value is 8-bit.
	 * @return true, if the represented value is 8-bit.
	 */
	public boolean is8Bit() {
		return true;
	}
	
	/**
	 * Indicates whether the represented value is 16-bit.
	 * @return true, if the represented value is 16-bit.
	 */
	public boolean i168Bit() {
		return false;
	}
	
}