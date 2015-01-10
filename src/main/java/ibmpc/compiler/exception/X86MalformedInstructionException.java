/**
 * 
 */
package ibmpc.compiler.exception;

import ibmpc.exceptions.X86AssemblyException;


/**
 * Represents the error that occurs when the compiler encounters
 * or invalid or malformed 80x86 instruction.
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86MalformedInstructionException extends X86AssemblyException {
	
	/**
	 * Creates a new instance of this exception
	 */
	public X86MalformedInstructionException() {
		super( "Invalid or malformed instruction" );
	}

	/**
	 * Creates a new instance of this exception
	 * @param message the cause of the error
	 */
	public X86MalformedInstructionException( final String message ) {
		super( message );
	}

}
