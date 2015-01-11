package org.ldaniels528.javapc.msdos.storage;

import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException;

/**
 * Represents an IBM PC/MS DOS I/O Device
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcInputOutputDevice {

	/**
	 * Opens this device for reading or writing
	 * @throws IbmPcException
	 */
	public void open() throws IbmPcException;
	
	/**
	 * Closes this device
	 * @throws IbmPcException
	 */
	public void close() throws IbmPcException;
	
	/**
	 * Reads a block of data from this device
	 * @return a block of data from this device
	 * @throws IbmPcException
	 */
	public byte[] read() throws IbmPcException;
	
	/**
	 * Writes a block of data to this device
	 * @param bytes the block of data to be written
	 * @throws IbmPcException
	 */
	public void write( byte[] bytes ) throws IbmPcException;
	
}
