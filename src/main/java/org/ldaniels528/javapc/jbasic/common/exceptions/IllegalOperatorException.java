package org.ldaniels528.javapc.jbasic.common.exceptions;

/**
 * Illegal Operator Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class IllegalOperatorException extends RuntimeException {

	/**
	 * Default Constructor
	 */
	public IllegalOperatorException() {
		super( "Illegal operator" );
	}

}
