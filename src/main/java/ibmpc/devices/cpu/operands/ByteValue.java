package ibmpc.devices.cpu.operands;

/**
 * Represents an unsigned byte (8-bit) operand value
 * @author lawrence.daniels@gmail.com
 */
public class ByteValue implements OperandValue {
	public static final ByteValue ONE = new ByteValue( 1 );
	private final int value;
	
	/**
	 * Creates a new unsigned byte (8-bit) operand value
	 * @param value the given value
	 */
	public ByteValue( final int value ) {
		this.value = value & 0x00FF;
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
		return SIZE_8BIT;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "%02X", value );
	}

}
