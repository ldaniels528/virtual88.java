package ibmpc.devices.cpu.x86.opcodes.data;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.ByteValue;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Escape
 * 
 * Usage:  ESC     immed,src
 * Modifies flags: None
 *  
 * Provides access to the data bus for other resident processors.
 * The CPU treats it as a NOP but places memory operand on bus.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class ESC extends AbstractOpCode {
	public static final ByteValue ESC_09 = new ByteValue( 0x09 );
	public static final ByteValue ESC_19 = new ByteValue( 0x19 );
	public static final ByteValue ESC_1C = new ByteValue( 0x1C );
	public static final ByteValue ESC_1E = new ByteValue( 0x1E );
	public static final ByteValue ESC_29 = new ByteValue( 0x29 );
	public static final ByteValue ESC_2D = new ByteValue( 0x2D );
	public static final ByteValue ESC_39 = new ByteValue( 0x29 );
	private final Operand immed;
	private final Operand src;
	
	/**
	 * ESC immed, src
	 * @param dest the given {@link Operand destination}
	 * @param src the given {@link Operand source}
	 */
	public ESC( final Operand dest, final Operand src ) {
		this.immed	= dest;
		this.src	= src;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// TODO figure this out
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "ESC %s,%s", immed, src );
	}
	
}
