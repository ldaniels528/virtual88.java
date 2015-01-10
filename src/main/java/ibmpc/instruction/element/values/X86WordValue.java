package ibmpc.instruction.element.values;

import ibmpc.devices.memory.X86MemoryUtil;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a 16-bit value
 * @author lawrence.daniels@gmail.com
 */
public class X86WordValue extends X86NumericValue {
	
	/**
	 * Creates a new 16-bit 80x86 value
	 * @param value the given 16-bit value
	 */
	public X86WordValue( final int value ) {
		super( value & 0xFFFF ); // mask off upper portion
	}
	
	/**
	 * Creates a new 16-bit 80x86 value
	 * @param value the given 16-bit value
	 */
	public X86WordValue( final String value ) {
		this( (int)GwBasicValues.parseNumericString( value ) );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.values.X86Value#getBytes()
	 */
	public byte[] getBytes() {
		final byte hiByte = X86MemoryUtil.getHighByte( value );
		final byte loByte = X86MemoryUtil.getLowByte( value );
		return new byte[] { loByte, hiByte };
	}
	
	/**
	 * Returns the high byte of the value
	 * @return the high byte of the value
	 */
	public byte getHighByte() {
		return X86MemoryUtil.getHighByte( value );
	}
	
	/**
	 * Returns the low byte of the value
	 * @return the low byte of the value
	 */
	public byte getLowByte() {
		return X86MemoryUtil.getLowByte( value );
	}
	
	/**
	 * Indicates whether the represented value is 8-bit.
	 * @return true, if the represented value is 8-bit.
	 */
	public boolean is8Bit() {
		return false;
	}
	
	/**
	 * Indicates whether the represented value is 16-bit.
	 * @return true, if the represented value is 16-bit.
	 */
	public boolean i168Bit() {
		return true;
	}
	
}
