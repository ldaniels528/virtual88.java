package org.ldaniels528.javapc.jbasic.common.values;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode;

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
	 * @return the corresponding {@link org.ldaniels528.javapc.jbasic.common.values.Variable variable} to this reference from the given environment
	 */
	Variable getVariable( JBasicCompiledCodeReference program );

}
