/**
 * 
 */
package ibmpc.compiler.exception;

import ibmpc.exceptions.X86AssemblyException;

/**
 * Represents an "Illegal number format" Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86IllegalNumericFormatException extends X86AssemblyException {
	private final String number;

	/**
	 * Creates an instance of this exception
	 * @param number the given numeric string
	 */
	public X86IllegalNumericFormatException( String number ) {
		super( "Illegal number format '" + number + "'" );
		this.number = number;
	}
	
	/**
	 * @return the numeric string that caused this exception
	 */
	public String getNumber() {
		return number;
	}

}
