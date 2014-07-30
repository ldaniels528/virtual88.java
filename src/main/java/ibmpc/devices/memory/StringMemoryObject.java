package ibmpc.devices.memory;

/**
 * Represents a JBasic String
 * @author lawrence.daniels@gmail.com
 */
public interface StringMemoryObject extends MemoryObject {

	/**
	 * Appends the given object to this string
	 * @param object the given {@link MemoryObject object}
	 */
	void append( MemoryObject object );
	
	/**
	 * @return the current length of this string
	 */
	int length();
	
	/**
	 * Sets the contents of the given string into memory
	 * @param string the given string
	 */
	void setString( String string );
	
}