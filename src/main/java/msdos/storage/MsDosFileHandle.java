package msdos.storage;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.exceptions.IbmPcException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents an IBM PC/MS DOS File Handle
 * @author lawrence.daniels@gmail.com
 */
public class MsDosFileHandle {
	private RandomAccessFile raf;
	private File file;
	private int handleID;
	
	/**
	 * Creates an instance of this handle
	 * @param file the given {@link File file}
	 * @param handleID the given file handle
	 */
	public MsDosFileHandle( final File file, final int handleID ) {
		this.file 		= file;
		this.handleID	= handleID;
	}
	
	/**
	 * Opens this file handle
	 * @throws IbmPcException
	 */
	public void open( final MsDosFileAccessMode accessMode ) 
	throws IbmPcException {
		// get the persistence mode
		final String mode = getPersistenceMode( accessMode );
		
		// open the file
		try {
			raf = new RandomAccessFile( file, mode );
		} 
		catch( final IOException e ) {
			throw new IbmPcException( e );
		}
	}
	
	/**
	 * Closes this file handle
	 * @throws IbmPcException
	 */
	public void close() throws IbmPcException {
		if( raf != null ) {
			try {
				raf.close();
			} 
			catch( final IOException e ) {
				throw new IbmPcException( e );
			}
		}
	}

	public MemoryObject read() throws IbmPcException {
		return null;
	}
	
	/**
	 * Writes the given data block to the device
	 * @param dataBlock the given data block
	 * @throws IbmPcException
	 */
	public void write( final byte[] dataBlock ) 
	throws IbmPcException {
		// attempt to write the data block to the file
		try {
			raf.write( dataBlock );
		} 
		catch( final IOException e ) {
			throw new IbmPcException( e );
		}
	}
	
	public void write( MemoryObject object ) throws IbmPcException {
		
	}
	
	/**
	 * @return the underlying file that is being accessed
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the file handle index
	 */
	public int getHandleID() {
		return handleID;
	}
	
	/**
	 * Returns the appropriate "java"-based persistence mode
	 * @return the appropriate "java"-based persistence mode
	 * @throws IllegalStateException
	 */
	private String getPersistenceMode( final MsDosFileAccessMode accessMode )  {
		if( accessMode.isReadWrite() ) return "rw";
		else return "r";
	}
	
}