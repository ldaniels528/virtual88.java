package org.ldaniels528.javapc.jbasic.common.values.impl;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

/**
 * Represents a reference to a JBasic variable
 */
public class SimpleVariableReference implements VariableReference {
	  private final String name;

	  /**
	   * Creates a reference to the variable of the given name
	   * @param name the name of the variable being reference by this object
	   */
	  public SimpleVariableReference( String name ) {
	    this.name = name;	    
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.base.values.ReferencedEntity#getName()
	   */
	  public String getName() {
	    return name;
	  } 
	  
	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.base.values.Value#getValue(org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	   */
	  public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		  // get an instance of the variable
		  final Variable variable = getVariable( program );
		  
		  // return the variable's content
		  return variable.getValue( program );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.base.values.VariableReference#getVariable(org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	   */
	  public Variable getVariable( JBasicCompiledCodeReference program ) {
		  return program.getVariable( this );
	  }

	  /**
	   * @return  a string representation of this object.
	   */
	  public String toString() {
		  return getName();
	  }
}
