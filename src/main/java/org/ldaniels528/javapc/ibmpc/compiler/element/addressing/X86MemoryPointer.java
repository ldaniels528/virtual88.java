package org.ldaniels528.javapc.ibmpc.compiler.element.addressing;

/**
 * Represents an 80x86 memory pointer (e.g. 'byte ptr [bx]')
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86MemoryPointer extends X86MemoryAddress {
	protected final X86ReferencedAddress reference;
	
	/**
	 * Creates a new 80x86 memory pointer
	 * @param reference the given {@link X86ReferencedAddress referenced address}
	 */
	public X86MemoryPointer( final X86ReferencedAddress reference ) {
		this.reference = reference;
	}

	/**
	 * @return the reference
	 */
	public X86ReferencedAddress getReference() {
		return reference;
	}
	
	/**
	 * Indicates whether the pointer is 8-bit (e.g. "byte ptr [bx]")
	 * @return true, if the pointer is 8-bit
	 */
	public abstract boolean is8Bit();
	
	/**
	 * Indicates whether the pointer is 16-bit (e.g. "word ptr [bx]")
	 * @return true, if the pointer is 16-bit
	 */
	public abstract boolean is16Bit();
	
	/**
	 * Indicates whether the pointer is 32-bit (e.g. "dword ptr [bx]")
	 * @return true, if the pointer is 32-bit
	 */
	public abstract boolean is32Bit();
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.compiler.encoder.addressing.X86MemoryAddress#isMemoryPointer()
	 */
	@Override
	public final boolean isMemoryPointer() {
		return true;
	}

}
