package ibmpc.devices.cpu.operands;

/**
 * Represents a signed byte (8-bit) operand value
 * @author lawrence.daniels@gmail.com
 */
public class SignedByteValue implements OperandValue {
	public static final ByteValue ONE = new ByteValue( 1 );
	private final int value;
	private final int signedValue;
	
	/**
	 * Creates a new signed byte (8-bit) operand value
	 * @param value the given value
	 */
	public SignedByteValue( final int value ) {
		this.value 			= value & 0x00FF; 
		this.signedValue	= toSignedByte( this.value );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.opcode.X86Operand#get()
	 */
	public int get() {
		return signedValue;
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
		// is the value negative? (mask = 1000 0000)
		final boolean negative = ( value & 0x80 ) > 0;
		
		// normalize the value for display
		final int signedValue = negative ? ( 0x100 - value ) : value;
		
		// return the formatted string
		return String.format( "%c%02X", negative ? '-' : '+', signedValue );
	}
	
	private int toSignedByte( final int value ) {
		final int unsignedValue = ( value & 0x00FF );
		final int signedValue = unsignedValue > 0x80 ? -( 0x100 - unsignedValue ) : unsignedValue;
		return signedValue;
	}

}
