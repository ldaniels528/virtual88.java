package jbasic.common.values;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.program.JBasicSourceCode;

/**
 * Represents a reference to a variable
 */
public interface VariableReference extends Value {
	
	/**
	 * @return the name of this value
	 */
	String getName();
  
	/**
	 * Returns the corresponding variable to this reference from the given environment
	 * @param program the currently running {@link JBasicSourceCode program}
	 * @return the corresponding {@link jbasic.common.values.Variable variable} to this reference from the given environment
	 */
	Variable getVariable( JBasicCompiledCodeReference program );

}
