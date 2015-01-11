package org.ldaniels528.javapc.jbasic.common.values;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

/**
 * Represents a JBasic Variable
 */
public interface Variable extends Value {
	
	/**
	 * @return the name of this value
	 */
	String getName();
	
	/**
	 * @return the underlying memory object
	 */
	MemoryObject getValueObject();

	/**
	 * Mutator method to set the value of this variable
	 * @param value the given {@link MemoryObject value}
	 */
	void setValue( MemoryObject value );

}
