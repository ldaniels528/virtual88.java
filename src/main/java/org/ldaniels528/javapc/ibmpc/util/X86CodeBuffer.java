package org.ldaniels528.javapc.ibmpc.util;

import org.apache.log4j.Logger;

import static org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryUtil.getHighByte;
import static org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryUtil.getLowByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.lang.String.format;

/**
 * This class represents a 80x86 Instruction Set Encoder
 * @author lawrence.daniels@gmail.com
 */
public class X86CodeBuffer {
	private final Logger logger = Logger.getLogger(getClass());
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
	 * @param byteCode the given byte code
	 */
	public X86CodeBuffer setByte( final int byteCode ) {
		bytes.write( byteCode );
		logger.debug(format("byte %02X", byteCode));
		return this;
	}
	
	/**
	 * Sets the given bytes starting at the next memory offset
	 * @param opCodes the given byte codes
	 */
	public X86CodeBuffer setBytes( final int ... opCodes ) throws IOException {
		// create the byte code from the opCodes
		final byte[] byteCode = new byte[ opCodes.length ];
		for( int n = 0; n < byteCode.length; n++ ) {
			byteCode[n] = (byte)opCodes[n];
		}
		
		// write the bytes to memory
		setBytes( byteCode );
		return this;
	}
	
	/**
	 * Sets the given bytes starting at the next memory offset
	 * @param bytecodes the given byte codes
	 */
	public X86CodeBuffer setBytes( final byte ... bytecodes ) throws IOException {
		bytes.write( bytecodes );
		logger.debug( bytecodes );
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
		  logger.debug(format("word %04X", wordcode));
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