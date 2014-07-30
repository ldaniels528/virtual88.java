package msdos.storage;


import ibmpc.exceptions.IbmPcException;

/**
 * Represents a File Handle Not Found Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class MsDosFileHandleNotFoundException extends IbmPcException {

	/**
	 * Creates a new "File Handle Not Found" exception
	 * @param fileHandleID the ID of the file handle that caused the exception
	 */
	public MsDosFileHandleNotFoundException( final int fileHandleID ) {
		super( String.format( "File handle '%04Xh' not found", fileHandleID ) );
	}

}
