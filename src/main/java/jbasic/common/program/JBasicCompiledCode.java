package jbasic.common.program;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.OutOfMemoryException;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableTypeDefinition;
import jbasic.common.values.impl.SimpleConstant;

/**
 * Represents a compiled JBasic Program
 * @author lawrence.daniels@gmail.com
 */
public interface JBasicCompiledCode extends JBasicCompiledCodeReference {

	/////////////////////////////////////////////////////////
	//		Service Method(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Adds the given value to this environment as queued values
	 * @param constant the given {@link jbasic.common.values.impl.SimpleConstant constant value}
	 */
	void addData( SimpleConstant constant );
	
	/**
	 * Adds the given type definition to this program
	 * @param typeDef the given {@link VariableTypeDefinition type definition}
	 */
	void add( VariableTypeDefinition typeDef );
	
	  /**
	   * Maps the given label to the current position 
	   * with the compiled code.
	   * @param lineNumber the given label 
	   */
	void addLabel( String label );
	
	/**
	 * Assembles the given code in memory, returning the offset in memory
	 * where the code can be called
	 * @param asmCode the given assembly code
	 * @throws JBasicException 
	 */
	int assemble( String ... asmCode ) throws JBasicException;	
	
	/**
	 * Determines the variable type based on the variable's name
	 * @param name the given name of the {@link Variable variable}
	 * @return the variable type identifier
	 * @throws OutOfMemoryException 
	 */
	MemoryObject createObject( String name ) throws OutOfMemoryException;
	
	/**
	 * Executes the compiled code
	 * @throws JBasicException
	 */
	void execute() throws JBasicException;
	
	/**
	 * Determines the variable type based on the variable's name
	 * @param name the given name of the {@link Variable variable}
	 * @return the variable type identifier
	 */
	MemoryObject wrapObject( String name, int offset );
	
	/**
	 * Invokes the assembly code found at the given offset in memory
	 * @param offset the given offset in memory
	 * @throws JBasicException
	 */
	void executeAssembly( int offset ) throws JBasicException;
	
	/**
	 * @return the next values value from the values queue
	 * @throws JBasicException
	 */
	SimpleConstant getNextData() throws JBasicException;
	
	/**
	 * Generates a new random sequence based on the given seed
	 * @param seed the given seed
	 */
	void randomize( long seed );
	
	/**
	 * Resets the values pointer back to the beginning of the values segment
	 */
	void resetDataPointer();
	
	/////////////////////////////////////////////////////////
	//		Array Variable Method(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Adds the given variable array to this environment
	 * @param array the given {@link jbasic.common.values.VariableArray variable array}
	 */
	void createArrayVariable( String arrayName, int arraySize ) 
	throws OutOfMemoryException;
	
	/**
	 * Deallocates the given array.
	 * @param arrayName the name of the array to deallocate.
	 * @throws JBasicException 
	 */
	void destroyVariableArray( String arrayName ) throws JBasicException;

	/////////////////////////////////////////////////////////
	//		Variable Method(s)
	/////////////////////////////////////////////////////////

	/**
	 * Adds the given variable to this environment
	 * @param variable the given {@link jbasic.common.values.Variable variable}
	 */
	void createVariable( Variable variable );
	
	/**
	 * Deallocates the given variable.
	 * @param variableName the name of the variable to deallocate.
	 * @throws JBasicException 
	 */
	void destroyVariable( String variableName ) throws JBasicException;
	
	/////////////////////////////////////////////////////////
	//		Control Flow Method(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Setups a iteration sequence (FOR - NEXT loop)
	 * @param variable the loop {@link Variable control variable}
	 * @param opCode the given {@link ConditionalOpCode conditional opCode}
	 */
	void conditionalControlBegin( Variable variable, ConditionalOpCode opCode );

	/**
	 * Re-evaluates the loop to determine whether to continue looping
	 * @param program the given {@link JBasicSourceCode program}
	 * @param variable the loop control {@link Variable variable}
	 */
	void conditionalControlIterate( JBasicCompiledCode program, Variable variable ) 
	throws JBasicException;
	
	/**
	 * Returns the last updated condition control block
	 * @return the last updated {@link ConditionalControlBlock condition control block}
	 */
	Variable getLastControlStackVariable();
	
	/**
	 * Causes the execution of the program to change to the given label
	 * @param label to the given label
	 * @param returns indicates whether an entry should be made on the call stack to setup a return
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	void gotoLabel( String label, boolean returns ) throws JBasicException;
	
	/**
	 * Resets the call stack for returning from a subroutine.
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	void popFromCallStack() throws JBasicException;
	
	/////////////////////////////////////////////////////////
	//		Accessor and Mutator Method(s)
	/////////////////////////////////////////////////////////

	/**
	 * Sets the default memory segment
	 * @param defaultSegment the default memory segment
	 */
	void setDefaultMemorySegment( int defaultSegment );
	
}
