package ibmpc.devices.cpu.x86.opcodes.data;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Define Byte (DB) MACRO Instruction
 * @author ldaniels
 */
public class DB extends AbstractOpCode {
	private int value;
	
	/**
	 * Creates a new define byte instruction
	 * @param byteValue the given byte value
	 */
	public DB( final int byteValue ) {
		this.value = byteValue;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// do nothing
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "DB %02X", value );
	}

}
