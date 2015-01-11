package org.ldaniels528.javapc.jbasic.common.exceptions;

import org.ldaniels528.javapc.jbasic.common.program.OpCode;

/**
 * Represents a GWBASIC "Illegal Function Call" Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class IllegalFunctionCallException extends RuntimeException {

	/**
	 * Default constructor
	 */
	public IllegalFunctionCallException() {
		super( "Illegal Function Call" );
	}
	
	/**
	 * Default constructor
	 */
	public IllegalFunctionCallException( OpCode opCode ) {
		super( "Illegal Function Call" );
	} 

	
}
