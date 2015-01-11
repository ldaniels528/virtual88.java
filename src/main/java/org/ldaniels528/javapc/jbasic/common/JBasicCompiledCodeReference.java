package org.ldaniels528.javapc.jbasic.common;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableArrayIndexReference;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;

/**
 * Represents a block of compiled code
 * @author lawrence.daniels@gmail.com
 */
public interface JBasicCompiledCodeReference {
	
	/**
	 * @return the segment where the program code resides
	 */
	int getCodeSegment();
	
	/**
	 * @return the segment where the program data resides
	 */
	int getDataSegment();
	
	/**
	 * @return the default user memory segment.
	 */
	int getDefaultMemorySegment();
	
	/**
	 * @return the {@link IbmPcSystem system} that this program
	 * is running upon.
	 */
	IbmPcSystem getSystem();
	
	/**
	 * @return the memory manager
	 */
	MemoryManager getMemoryManager();
	
	/**
	 * Retrieves a variable by reference
	 * @param reference the given {@link org.ldaniels528.javapc.jbasic.common.values.VariableArrayIndexReference reference}
	 * @return the variable that corresponds to the given {@link org.ldaniels528.javapc.jbasic.common.values.Variable variable}
	 */
	Variable getArrayVariable( VariableArrayIndexReference reference );
	
	/**
	 * Retrieves a variable by reference
	 * @param reference the given {@link org.ldaniels528.javapc.jbasic.common.values.VariableReference reference}
	 * @return the variable that corresponds to the given {@link Variable variable}
	 */
	Variable getVariable( VariableReference reference );

}
