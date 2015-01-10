package ibmpc.instruction.element.addressing;


import ibmpc.instruction.element.registers.X86RegisterRef;

/**
 * Represents an 80x86 memory pointer (e.g. 'byte ptr [bx]')
 * @author lawrence.daniels@gmail.com
 */
public class X86MemoryPointer8Bit extends X86MemoryPointer {

	/**
	 * Creates a new 8-bit 80x86 memory pointer
	 * @param reference the given {@link X86ReferencedAddress referenced address}
	 */
	public X86MemoryPointer8Bit( final X86ReferencedAddress reference ) {
		super( reference );
	}

	/* (non-Javadoc)
	 * @see ibmpc.compiler.encoder.addressing.X86MemoryAddress#getByteCode()
	 */
	@Override
	public byte[] getByteCode() {
		return reference.getByteCode();
	}

	/* (non-Javadoc)
	 * @see ibmpc.compiler.encoder.addressing.X86MemoryAddress#getByteCode(ibmpc.compiler.encoder.registers.X86RegisterRef)
	 */
	@Override
	public byte[] getByteCode( final X86RegisterRef register ) {
		return reference.getByteCode( register );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.compiler.encoder.addressing.X86MemoryPointer#is8Bit()
	 */
	public boolean is8Bit() {
		return true;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.compiler.encoder.addressing.X86MemoryPointer#is16Bit()
	 */
	public boolean is16Bit() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.compiler.encoder.addressing.X86MemoryPointer#is32Bit()
	 */
	public boolean is32Bit() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "BYTE PTR %s", reference );
	}

}
