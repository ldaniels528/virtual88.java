package jbasic.common.values;

import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SubscriptOutOfRangeException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.JBasicSourceCode;

/**
 * Represents a JBasic Variable Array
 */
public interface VariableArray {
	
	/**
	 * @return the name of this value
	 */
	String getName();

	/**
	 * Destroys this array freeing any held resources
	 */
	void destroy();
	
	/**
	 * Returns the {@link jbasic.common.values.Variable element} found at the given index
	 * @param index the given index within the array
	 * @return the {@link jbasic.common.values.Variable element} found at the given index
	 * @throws SubscriptOutOfRangeException
	 */
	Variable getElement( int index ) throws SubscriptOutOfRangeException;
  
	/**
	 * @return the number of elements in this array
	 */
	int length();
  
	/**
	 * Returns the values type containing the value of this element
	 * @param index the index of the element that is to be accessed
	 * @param program the currently running {@link JBasicSourceCode program}
	 * @return the {@link ibmpc.devices.memory.MemoryObject values type} containing the value of this element
	 * @throws JBasicException 
	 */
	MemoryObject getValue( int index, JBasicCompiledCode program )
	throws JBasicException;
  
	/**
	 * Mutator method to set the value of this variable
	 * @param index the index of the element that is to be set
	 * @param value the given value
	 * @throws JBasicException 
	 */
	void setValue( int index, MemoryObject value ) 
	throws JBasicException;

}
