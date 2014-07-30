package ibmpc.devices.memory;

import ibmpc.exceptions.IbmPcUnexpectedErrorException;

/**
 * This exception occurs when JBasic is "out of memory"
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class OutOfMemoryException extends IbmPcUnexpectedErrorException {
	private final MemoryObject object;
	
	/**
	 * Default Constructor
	 * @param object the object that caused this exception
	 */
	public OutOfMemoryException( final MemoryObject object ) {
		super( "Out of memory" );
		this.object = object;
	}
	
	/**
	 * @return the object that caused this exception
	 */
	public MemoryObject getObject() {
		return object;
	}

}
