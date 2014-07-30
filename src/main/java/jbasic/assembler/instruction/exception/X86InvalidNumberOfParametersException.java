/**
 * 
 */
package jbasic.assembler.instruction.exception;

/**
 * 80x86 Malformed Instruction Exception - Invalid number of parameters 
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86InvalidNumberOfParametersException extends X86MalformedInstructionException {

	/**
	 * Creates a new invalid number of parameters exception
	 * @param expectedCount the expected number of parameters
	 * @param actualCount the actual number of parameters
	 */
	public X86InvalidNumberOfParametersException( final int expectedCount, final int actualCount ) {
		super( "Invalid number parameters; found " + actualCount + " expected " + expectedCount );
	}
	
}
