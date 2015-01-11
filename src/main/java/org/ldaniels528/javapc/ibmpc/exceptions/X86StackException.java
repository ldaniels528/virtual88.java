/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.exceptions;

/**
 * Represents an exception that occurs when
 * an attempt to reference the stack is made
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86StackException extends RuntimeException {

	/**
	 * Default constructor
	 */
	public X86StackException() {
		super( "Stack is empty" );
	}
	
}
