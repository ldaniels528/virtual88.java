package ibmpc.devices.cpu.x86.registers;

/**
 * Represents a composite register; a register composed of
 * two parts, high and low respectively. (e.g. AX = AH:AL)
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86CompositeRegister implements X86Register {
	private final X86Register hi;
	private final X86Register lo;
	private final String name;
	private final int shift;
	private final int index;

	/**
	 * Default Constructor
	 * @param name the name of this register
	 * @param hi the high portion of this register
	 * @param lo the low portion of this register
	 * @param shift the number of bits to shift in compositing the values
	 * @param index the given register index
	 */
	public X86CompositeRegister( String name, X86Register hi, X86Register lo, int shift, int index ) {
		this.name 	= name;
		this.hi 	= hi;
		this.lo 	= lo;
		this.shift	= shift;
		this.index	= index;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.assembly.JBasicRegister#get()
	 */
	public int get() {
		final int value = ( hi.get() << shift ) | lo.get();		
		return value;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.assembly.JBasicRegister#set(int)
	 */
	public void set( int value ) {
		hi.set( ( value & 0xff00 ) >> shift );
		lo.set( value & 0x00ff );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.assembly.intepreter.registers.X86Register#getIndex()
	 */
	public int getIndex() {
		return index;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.assembly.registers.X86Register#setBit(int, boolean)
	 */
	public void setBit( int bitNum, boolean on ) {
		// get the current value
		final int oldValue = get();
		
		// turn the bit on?
		if( on ) {
			final int mask = bitNum << 1;			
			set( oldValue | mask );
		}
		// turn the bit off...
		else {
			final int mask = 0xFFFF - ( bitNum << 1 );
			set( oldValue & mask );
		}		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.assembly.JBasicRegister#delta(int)
	 */
	public int add( int delta ) {
		final int value0 = get();
		final int value1 = value0 + delta;
		set( value1 );
		return value1;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.assembly.registers.X86Register#and(int)
	 */
	public void and( int mask ) {
		// get the current value in the register
		final int value0 = get();
		
		// calculate the AND'ed value
		final int value1 = ( value0 & mask );
		
		// put the AND'ed value back into the register
		set( value1 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.assembly.registers.X86Register#or(int)
	 */
	public void or( int mask ) {
		// get the current value in the register
		final int value0 = get();
		
		// calculate the OR'ed value
		final int value1 = ( value0 | mask );
		
		// put the OR'ed value back into the register
		set( value1 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.assembly.registers.X86Register#lshift(int)
	 */
	public void lshift( int bits ) {
		// get the current value in the register
		final int value0 = get();
		
		// calculate the shifted value
		final int value1 = ( value0 << bits );
		
		// put the shifted value back into the register
		set( value1 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.assembly.registers.X86Register#rshift(int)
	 */
	public void rshift( int bits ) {
		// get the current value in the register
		final int value0 = get();
		
		// calculate the shifted value
		final int value1 = ( value0 >> bits );
		
		// put the shifted value back into the register
		set( value1 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
	
}
