package ibmpc.devices.cpu.operands;

/**
 * X86 16-bit Operand Value
 * @author lawrence.daniels@gmail.com
 */
public class WordValue implements OperandValue {
	private final int value;
	
	/**
	 * Creates a new 16-bit operand value
	 * @param value the given value
	 */
	public WordValue( final int value ) {
		this.value = ( value & 0xFFFF );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.opcode.X86Operand#get()
	 */
	public int get() {
		return value;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.opcode.X86Operand#set(int)
	 */
	public void set( final int value ) {
		throw new IllegalStateException( "Cannot be set" );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.X86Operand#size()
	 */
	public int size() {
		return SIZE_16BIT;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "%04X", value );
	}

}
