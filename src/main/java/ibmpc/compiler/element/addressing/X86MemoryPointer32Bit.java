/**
 * 
 */
package ibmpc.compiler.element.addressing;

import ibmpc.compiler.element.registers.X86RegisterRef;

/**
 * Represents an 80x86 memory pointer (e.g. 'dword ptr [bx]')
 * @author lawrence.daniels@gmail.com
 */
public class X86MemoryPointer32Bit extends X86MemoryPointer {
	
	/**
	 * Creates a new 32-bit 80x86 memory pointer
	 * @param reference the given {@link X86ReferencedAddress referenced address}
	 */
	public X86MemoryPointer32Bit( final X86ReferencedAddress reference ) {
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
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean is32Bit() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format( "DWORD PTR %s", reference );
	}

}
