package ibmpc.devices.memory;

/**
 * Represents an Integer, Single Precision, or Double Precision Number
 * @author lawrence.daniels@gmail.com
 */
public interface NumberMemoryObject extends MemoryObject {		
	
	/**
	 * Sets the contents of the given integer into memory
	 * @param value the given integer value
	 */
	void setValue( double value );
	
	//////////////////////////////////////////////////////
	//    Mathematical Method(s)
	//////////////////////////////////////////////////////
	
	/**
	 * Adds the given numeric value to this object 
	 * @param value the given numeric value
	 * @return an object containing the sum of the two values 
	 */
	NumberMemoryObject add( NumberMemoryObject value );
	
	/**
	 * Divides the given numeric value to this object 
	 * @param value the given numeric value
	 * @return an object containing the sum of the two values 
	 */
	NumberMemoryObject divide( NumberMemoryObject value );
	
	/**
	 * Raises the value of this object by the given exponent 
	 * @param value the given numeric value
	 * @return an object containing the sum of the two values 
	 */
	NumberMemoryObject exponent( NumberMemoryObject value );
	
	/**
	 * Performs a modulus operation between the given numeric value and this object 
	 * @param value the given numeric value
	 * @return an object containing the sum of the two values 
	 */
	NumberMemoryObject modulus( NumberMemoryObject value );
	
	/**
	 * Multiplies the given numeric value to this object 
	 * @param value the given numeric value
	 * @return an object containing the sum of the two values 
	 */
	NumberMemoryObject multiply( NumberMemoryObject value );
	
	/**
	 * Subtracts the given numeric value to this object 
	 * @param value the given numeric value
	 * @return an object containing the sum of the two values 
	 */
	NumberMemoryObject subtract( NumberMemoryObject value );

}
