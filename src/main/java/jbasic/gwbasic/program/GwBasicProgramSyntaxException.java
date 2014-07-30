package jbasic.gwbasic.program;

import jbasic.common.exceptions.JBasicException;

/**
 * An exception generated at the time of executing a
 * line numbered program
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class GwBasicProgramSyntaxException extends JBasicException {
	private Integer lineNumber;

	/**
	 * Creates an instance of this exception
	 * @param cause the cause of this exception
	 * @param lineNumber the line number of the instruction that failed
	 */
	public GwBasicProgramSyntaxException( final Exception cause, final Integer lineNumber ) {
		super( cause );
		this.lineNumber = lineNumber;
	}
	
	/**
	 * @return the line number that generated this exception
	 */
	public Integer getLineNumber() {
		return lineNumber;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getCause().toString() + " on line " + lineNumber; 
	}
	
}