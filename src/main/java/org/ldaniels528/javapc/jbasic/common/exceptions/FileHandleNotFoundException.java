package org.ldaniels528.javapc.jbasic.common.exceptions;

/**
 * Exception that occurs when a read, write, or close is attempted on
 * a non-existant file handle.
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class FileHandleNotFoundException extends JBasicException {
	private int fileHandle;

	/** 
	 * Creates an instance of this exception
	 * @param fileHandle the file handle that caused this exception
	 */
	public FileHandleNotFoundException( int fileHandle ) {
		super( "File handle does not exist" );
		this.fileHandle = fileHandle;
	}
	
	public int getFileHandle() {
		return fileHandle;
	}
	
}
