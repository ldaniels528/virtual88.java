package org.ldaniels528.javapc.ibmpc.exceptions;

/**
 * Represents the exception that is thrown when an 
 * attempt to parse an string into a numeric value
 * fails.
 * @author ldaniels
 */
@SuppressWarnings("serial")
public class IbmPcNumericFormatException extends IbmPcException {
	private String numericString;
	
	/**
	 * Creates a new numeric format exception
	 * @param numericString the given numeric string
	 */
	public IbmPcNumericFormatException( final String numericString ) {
		super( String.format( "Invalid numeric string '%s'", numericString ) );
		this.numericString = numericString;
	}
	
	/**
	 * @return the numeric string
	 */
	public String getNumericString() {
		return numericString;
	}
	
}
