package org.ldaniels528.javapc.jbasic.common.exceptions;

/**
 * This exception is caused by a reference to an index outside 
 * the defined sized of the array.
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class SubscriptOutOfRangeException extends RuntimeException {

	/**
	 * Default Constructor
	 */
	public SubscriptOutOfRangeException( String name, int index ) {
		super( "Subscript " + index +" is out of range for " + name );
	}
	
}
