package jbasic.assembler.util;

import static ibmpc.devices.memory.X86MemoryUtil.getHighByte;
import static ibmpc.devices.memory.X86MemoryUtil.getLowByte;

import ibmpc.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class represents a 80x86 Instruction Set Encoder
 * @author lawrence.daniels@gmail.com
 */
public class X86CodeBuffer {
	private final ByteArrayOutputStream bytes;
	
	///////////////////////////////////////////////////////////////////
	//			Constructor(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor
	 */
	public X86CodeBuffer( final int bufferSize ) {
		this( new ByteArrayOutputStream( bufferSize ) );
	}
	
	/**
	 * Default constructor
	 */
	public X86CodeBuffer( final ByteArrayOutputStream baos ) {
		this.bytes = baos;
	}
	
	///////////////////////////////////////////////////////////////////
	//			Service method(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the given byte in the next memory offset
	 * @param bytecode the given byte code
	 */
	public X86CodeBuffer setByte( final int byteCode ) {
		bytes.write( byteCode );
		Logger.debug( "byte %02X\n", byteCode );
		return this;
	}
	
	/**
	 * Sets the given bytes starting at the next memory offset
	 * @param bytecodes the given byte codes
	 */
	public X86CodeBuffer setBytes( final int ... opcodes ) {
		// create the byte code from the opCodes
		final byte[] bytecode = new byte[ opcodes.length ];
		for( int n = 0; n < bytecode.length; n++ ) {
			bytecode[n] = (byte)opcodes[n]; 
		}
		
		// write the bytes to memory
		setBytes( bytecode );
		return this;
	}
	
	/**
	 * Sets the given bytes starting at the next memory offset
	 * @param bytecodes the given byte codes
	 */
	public X86CodeBuffer setBytes( final byte ... bytecodes ) {
		try {
			bytes.write( bytecodes );
		} 
		catch ( final IOException e ) {
			e.printStackTrace();
		}

		Logger.debug( bytecodes );
		return this;
	}
	
	/**
	 * Sets the given word in the next memory offset
	 * @param wordcode the given word code (2 bytes)
	 */
	public X86CodeBuffer setWord( final int wordcode ) {
		  // determine high and low bytes of the word
		  final byte hiByte = getHighByte( wordcode );
		  final byte loByte = getLowByte( wordcode );
				
		  // write the bytes
		  bytes.write( loByte );
		  bytes.write( hiByte );
		  Logger.debug( "word %04X\n", wordcode );
		  return this;
	}
	
	public X86CodeBuffer setShortOffset( final int offset ) {
		return setByte( bytes.size() - offset );
	}

	/**
	 * @return the instructions contained within this object as a byte array
	 */
	public byte[] toByteArray() {
		final byte[] b = bytes.toByteArray();
		bytes.reset();
		return b;
	}
	
}