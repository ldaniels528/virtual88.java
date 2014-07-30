/**
 * 
 */
package jbasic.assembler.instruction.element.values;



/**
 * Represents an 80x86 Byte or Word value
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86NumericValue extends X86Value {
	protected final int value;
	
	/**
	 * Creates a new 8/16-bit 80x86 value
	 * @param value the given 8/16-bit value
	 */
	public X86NumericValue( final int value ) {
		this.value = value;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.values.X86Value#getValue()
	 */
	public final Integer getValue() {
		return value;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.compiler.encoder.values.X86Value#isNumeric()
	 */
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
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( is8Bit() ? "%02X" : "%04X", value );  
	}

}
