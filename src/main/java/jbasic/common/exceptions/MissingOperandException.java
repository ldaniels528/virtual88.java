package jbasic.common.exceptions;

/**
 * BASIC "Missing Operand" Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class MissingOperandException extends JBasicException {

	/**
	 * Default Constructor
	 */
	public MissingOperandException() {
		super( "Missing Operand" );
	}

}
