package ibmpc.devices.memory;

/**
 * Represent an in-memory data object
 */
public interface MemoryObject extends Comparable {
	
	/**
	 * Disposes of this object
	 */
	void destroy();
	
	/**
	 * Creates a duplicate of this object
	 * @return a duplicate of this {@link MemoryObject object}
	 */
	MemoryObject duplicate();
	
	/**
	 * Returns the content of this object
	 * @return the content of this object
	 */
	Object getContent();
	
	/**
	 * Sets the given content into this object
	 * @param content the given content
	 */
	void setContent( Object content );
	
	/**
	 * @return the offset in memory for this object
	 */
	int getOffset();
	
	/**
	 * @return the segment in memory for this object
	 */
	int getSegment();
	
	/**
	 * Initializes this memory object
	 */
	void initialize();
	
	/**
	 * Determines whether the given object is "type" 
	 * compatible with this object.
	 * @param object the given {@link MemoryObject object}
	 * @return true, if the two values are compatible
	 */
	boolean isCompatibleWith( MemoryObject object );
	
	/**
	 * @return true, if this value is an integer or a double
	 */
	boolean isNumeric();
	  
	/**
	 * @return true, if this value is an string
	 */
	boolean isString();
	
	/**
	 * @return the [allocated] length of this object
	 */
	int size();
		
	/**
	 * @return the given number as a double precision decimal value
	 */
	double toDoublePrecision();
	
	/**
	 * @return the given number as an integer value
	 */
	int toInteger();

	/**
	 * @return the given number as a single precision decimal value
	 */
	float toSinglePrecision();
	
	/**
	 * @return the given number as a string value
	 */
	String toString();

}