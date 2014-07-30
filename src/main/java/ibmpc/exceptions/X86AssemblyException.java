package ibmpc.exceptions;



/**
 * Base exception class for all X86 Assembly Exceptions
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86AssemblyException extends IbmPcException {

	public X86AssemblyException( String message ) {
		super(message);
	}
	
	public X86AssemblyException( Throwable cause ) {
		super( cause );
	}

}
