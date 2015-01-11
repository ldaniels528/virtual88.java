package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers;

/**
 * Represents a n-bit register
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86GenericRegister implements X86Register {
	protected final String name;
	protected final int mask;
	private final int index;
	protected int value;
	
	/**
	 * Creates an instance of this register
	 * @param name the name of this register
	 * @param mask the mask that will determine the size of this register
	 * @param index the given index of this register
	 */
	public X86GenericRegister( final String name, final int mask, final int index ) {
		this.name 	= name;
		this.mask	= mask;
		this.index	= index;
		this.value	= 0;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.assembly.JBasicRegister#get()
	 */
	public int get() {
		return value;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.assembly.JBasicRegister#set(int)
	 */
	public void set( int value ) {
		this.value = ( value & mask);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.assembly.intepreter.registers.X86Register#getIndex()
	 */
	public int getIndex() {
		return index;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.assembly.registers.X86Register#setBit(int, boolean)
	 */
	public void setBit( int bitNum, boolean on ) {
		// turn the bit on?
		if( on ) {
			final int mask = bitNum << 1;
			value |= mask;
		}
		// turn the bit off...
		else {
			final int mask = 0xFFFF - ( bitNum << 1 );
			value &= mask;
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.assembly.JBasicRegister#delta(int)
	 */
	public int add(int delta) {
		this.value = ( ( value + delta ) & mask );
		return value;
	}		
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.assembly.registers.X86Register#and(int)
	 */
	public void and( int mask ) {
		this.value &= mask;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.assembly.registers.X86Register#or(int)
	 */
	public void or( int mask ) {
		this.value |= mask;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.assembly.registers.X86Register#lshift(int)
	 */
	public void lshift( int bits ) {
		this.value <<= bits;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.assembly.registers.X86Register#rshift(int)
	 */
	public void rshift( int bits ) {
		this.value >>= bits;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

}
