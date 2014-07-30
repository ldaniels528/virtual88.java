package jbasic.assembler.instruction.element.addressing;

import static ibmpc.devices.memory.X86MemoryUtil.getHighByte;
import static ibmpc.devices.memory.X86MemoryUtil.getLowByte;

import ibmpc.exceptions.X86AssemblyException;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a 80x86 absolute memory address 
 * referenced by segment and offset.
 * @author lawrence.daniels
 */
public class X86AbsoluteMemoryAddress extends X86RelativeMemoryAddress {
	private final int segment;
	
	/**
	 * Creates a new direct memory address instance
	 * @param segment the given segment portion of the memory address
	 * @param offset the given offset portion of the memory address
	 */
	protected X86AbsoluteMemoryAddress( final int segment, final int offset ) {
		super( offset );
		this.segment = segment;
	}

	/**
	 * Parses the given address string 
	 * @param address the given address string (e.g. "32FE:4567")
	 * @throws X86AssemblyException
	 */
	public static X86AbsoluteMemoryAddress parse( final String address ) 
	throws X86AssemblyException {
		// split the address
		final String[] pc = address.split( "[:]" );
		if( pc.length != 2 )
			throw new X86AssemblyException( String.format( "Invalid address '%s'", address ) );
		
		// get the segment and offset
		// TODO figure out a way to do this cleanly
		final int segment = GwBasicValues.parseIntegerString( pc[0] );
		final int offset = GwBasicValues.parseIntegerString( pc[1] );
		
		// return a new absolute memory address
		return new X86AbsoluteMemoryAddress( segment, offset );
	}
	
	/**
	 * Returns the segment portion of the address
	 * @return the segment
	 */
	public int getSegment() {
		return segment;
	}

	/* (non-Javadoc)
	 * @see ibmpc.assembly.encoder.memoryaddress.X86MemoryAddress#getByteCode()
	 */
	public byte[] getByteCode() {
		// get the offset
		final int offset = getOffset();
		
		// get the high and low bytes of the segment address
		final byte segHi = getHighByte( segment );
		final byte segLo = getLowByte( segment );
		
		// get the high and low bytes of the offset address
		final byte offHi = getHighByte( offset );
		final byte offLo = getLowByte( offset );
		
		// return the bytes
		return new byte[] { offLo, offHi, segLo, segHi };
	}

}
