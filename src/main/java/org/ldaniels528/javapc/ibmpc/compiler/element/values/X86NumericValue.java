/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler.element.values;



/**
 * Represents an 8086 Byte or Word value
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86NumericValue extends X86Value {
	protected final int value;
	
	/**
	 * Creates a new 8/16-bit 8086 value
	 * @param value the given 8/16-bit value
	 */
	public X86NumericValue( final int value ) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Integer getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isNumeric() {
		return true;
	}
	
	/**
	 * Indicates whether the represented value is 8-bit.
	 * @return true, if the represented value is 8-bit.
	 */
	public abstract boolean is8Bit();
	
	/**
	 * Indicates whether the represented value is 16-bit.
	 * @return true, if the represented value is 16-bit.
	 */
	public abstract boolean i168Bit();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( is8Bit() ? "%02X" : "%04X", value );  
	}

}
