package org.ldaniels528.javapc.jbasic.common.program;

import org.ldaniels528.javapc.ibmpc.devices.memory.OutOfMemoryException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import java.util.Collection;
import java.util.SortedSet;

/**
 * Represents a JBasic Program Source Code
 * @author lawrence.daniels@gmail.com
 */
public interface JBasicSourceCode {
	
	/////////////////////////////////////////////////////////
	//		Service Method(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Adds the given program language statement to this program
	 * @param statement the given {@link JBasicProgramStatement program statement}
	 */
	void add( JBasicProgramStatement stmt );	
	
	/**
	 * Clears the program that is currently reside in memory
	 * @throws OutOfMemoryException 
	 */
	void clear() throws OutOfMemoryException;
	
	/**
	 * @return the {@link IbmPcSystem environment} that this program
	 * is running within.
	 */
	IbmPcSystem getEnvironment(); 	// TODO try to remove
	
	/////////////////////////////////////////////////////////
	//		Accessor and Mutator Method(s)
	/////////////////////////////////////////////////////////

	/**
	 * Returns the statements from the program that is currently resides in
	 * memory
	 * @return the {@link SortedSet set} of {@link JBasicProgramStatement statements} that make up this program
	 */
	Collection<JBasicProgramStatement> getStatements();
	
}