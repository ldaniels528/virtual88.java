/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler.element.addressing;


import org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterRef;

/**
 * Represents an 80x86 memory pointer (e.g. 'word ptr [bx]')
 * @author lawrence.daniels@gmail.com
 */
public class X86MemoryPointer16Bit extends X86MemoryPointer {
	
	/**
	 * Creates a new 16-bit 80x86 memory pointer
	 * @param reference the given {@link X86ReferencedAddress referenced address}
	 */
	public X86MemoryPointer16Bit( final X86ReferencedAddress reference ) {
		super( reference );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getByteCode() {
		return reference.getByteCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getByteCode( final X86RegisterRef register ) {
		return reference.getByteCode( register );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean is8Bit() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean is16Bit() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean is32Bit() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( "WORD PTR %s", reference );
	}

}
