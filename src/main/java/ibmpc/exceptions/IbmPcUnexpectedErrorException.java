/**
 * 
 */
package ibmpc.exceptions;

/**
 * IBM PC Unexpected Error Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class IbmPcUnexpectedErrorException extends RuntimeException {

	public IbmPcUnexpectedErrorException( String message ) {
		super( message );
	}
	
}
