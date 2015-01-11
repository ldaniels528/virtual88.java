package org.ldaniels528.javapc.ibmpc.devices.ports;

import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.apache.log4j.Logger;

import static java.lang.String.format;

/**
 * Represents the hardware port interface for an IBM PC/XT/AT.
 * @author ldaniels
 */
public class IbmPcHardwarePorts {
	// base memory segment?
	private static final int DEFAULT_SEGMENT 	= 0x0040;
	
	// port addresses
	private static final int MEM_CTRLR_STATUS	= 0x060;
	private static final int MEM_CTRLR_OUTPUT	= 0x064;
	private static final int COM1				= 0x378;
	private static final int COM2				= 0x378;
	private static final int COM3				= 0x378;
	private static final int COM4				= 0x378;
	private static final int LPT1				= 0x378;
	
	// internal fields
	private final Logger logger = Logger.getLogger(getClass());
	private final IbmPcRandomAccessMemory memory;
	
	/**
	 * Default constructor
	 */
	public IbmPcHardwarePorts( final IbmPcRandomAccessMemory memory ) {
		this.memory	= memory;
	}
	
	/**
	 * Reads an 8-bit data value from the given port number
	 * @param port the given port number
	 * @return a byte or word from the specified port
	 */
	public int in8( final int port ) {
		return memory.getByte( DEFAULT_SEGMENT, port );
	}
	
	/**
	 * Reads a 16-bit data value from the given port number
	 * @param port the given port number
	 * @return a byte or word from the specified port
	 */
	public int in16( final int port ) {
		return memory.getWord( DEFAULT_SEGMENT, port );
	}
	
	/** 
	 * Writes the given value to the given port number
	 * @param port the given port number
	 * @param value the given data value
	 */
	public void out8( final int port, final int value ) {
		// only the first 10 bits are used
		final int offset = port & 0x03FF; // mask = 0000 0011 1111 1111
		
		// get the port value
		final int value0 = memory.getByte( DEFAULT_SEGMENT, offset );
		logger.info(format("Changed value at port %04X from %04X to %04X", port, value0, value));
		
		// set the new port value
		memory.setByte( DEFAULT_SEGMENT, offset, value );
	}
	
	/** 
	 * Writes the given value to the given port number
	 * @param port the given port number
	 * @param value the given data value
	 */
	public void out16( final int port, final int value ) {
		// only the first 10 bits are used
		final int offset = port & 0x03FF; // mask = 0000 0011 1111 1111
		
		// get the port value
		final int value0 = memory.getWord( DEFAULT_SEGMENT, offset );
		logger.info(format("Changed value at port %04X from %04X to %04X", port, value0, value));
		
		// set the new port value
		memory.setWord( DEFAULT_SEGMENT, offset, value );
	}
	
	/** 
	 * Writes the given value to the given port number
	 * @param port the given port number
	 * @param value the given data value
	 */
	public void out32( final int port, final int value ) {
		// only the first 10 bits are used
		final int offset = port & 0x03FF; // mask = 0000 0011 1111 1111
		
		// get the port value
		final int value0 = memory.getDoubleWord( DEFAULT_SEGMENT, offset );
		logger.info(format("Changed value at port %04X from %04X to %04X", port, value0, value));
		
		// set the new port value
		memory.setDoubleWord( DEFAULT_SEGMENT, offset, value );
	}

}
