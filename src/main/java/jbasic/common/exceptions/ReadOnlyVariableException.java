package jbasic.common.exceptions;

/**
 * This is exception is thrown upon an attempt to set 
 * a read-only (system) variable. 
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class ReadOnlyVariableException extends RuntimeException {

	public ReadOnlyVariableException() {
		super( "This variable is read-only" );
	}
	
}
