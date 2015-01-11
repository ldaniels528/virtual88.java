package org.ldaniels528.javapc.jbasic.common.exceptions;


/**
 * Represents a GWBASIC "Syntax Error" Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class SyntaxErrorException extends JBasicException {

	public SyntaxErrorException() {
		super( "Syntax Error" );
	}
	
	/* 
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Syntax Error";
	}
	
}
