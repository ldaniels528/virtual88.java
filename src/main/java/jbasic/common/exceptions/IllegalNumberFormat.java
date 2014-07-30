package jbasic.common.exceptions;

import jbasic.assembler.instruction.exception.X86IllegalNumericFormatException;

/**
 * Represents an "Illegal number format" Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class IllegalNumberFormat extends TypeMismatchException {
	private final String number;

	/**
	 * Creates an instance of this exception
	 * @param number the given numeric string
	 */
	public IllegalNumberFormat( String number ) {
		super( "Illegal number format '" + number + "'" );
		this.number = number;
	}
	
	public IllegalNumberFormat( final X86IllegalNumericFormatException e ) {
		super( e );
		this.number = e.getNumber();
	}
	
	/**
	 * @return the numeric string that caused this exception
	 */
	public String getNumber() {
		return number;
	}
	
}
