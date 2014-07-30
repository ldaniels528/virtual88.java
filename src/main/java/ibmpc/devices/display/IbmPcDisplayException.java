/**
 * 
 */
package ibmpc.devices.display;

import ibmpc.exceptions.IbmPcUnexpectedErrorException;

/**
 * Represents an illegal call for the current display mode
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class IbmPcDisplayException extends IbmPcUnexpectedErrorException {

	public IbmPcDisplayException( String message ) {
		super( message );
	}
	
}
