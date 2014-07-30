package ibmpc.devices.cpu.operands;

/**
 * X86 32-bit Operand Value
 * @author lawrence.daniels@gmail.com
 */
public class DoubleWordValue implements OperandValue {
	private final int value;
	
	/**
	 * Creates a new 32-bit operand value
	 * @param value the given value
	 */
	public DoubleWordValue( final int value ) {
		this.value = value;
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
		return SIZE_32BIT;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "%08X", value );
	}

}
