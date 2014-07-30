package jbasic.common.exceptions;

import ibmpc.devices.memory.StringMemoryObject;

/**
 * Represents a GWBASIC "String too long" exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class StringTooLongException extends RuntimeException {
	private StringMemoryObject string;

	/**
	 * Default constructor
	 */
	public StringTooLongException( StringMemoryObject string ) {
		super( "String too long" );
		this.string = string;
	}
	
	/**
	 * @return the {@link StringMemoryObject string} that caused the exception
	 */
	public StringMemoryObject getString() {
		return string;
	}
	
}
