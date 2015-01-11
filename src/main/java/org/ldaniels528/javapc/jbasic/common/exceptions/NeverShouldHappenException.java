package org.ldaniels528.javapc.jbasic.common.exceptions;

/**
 * Represents an exception that should never occur
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class NeverShouldHappenException extends RuntimeException {

	public NeverShouldHappenException() {
		super( "Unexpected Exception" );
	}
	
	public NeverShouldHappenException( Exception e ) {
		super( "Unexpected Exception", e );
	}
	
}
