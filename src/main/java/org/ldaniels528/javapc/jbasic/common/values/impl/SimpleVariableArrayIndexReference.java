package org.ldaniels528.javapc.jbasic.common.values.impl;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableArrayIndexReference;

/**
 * Represents a reference to an index within a variable array
 */
public class SimpleVariableArrayIndexReference extends SimpleVariableReference 
implements VariableArrayIndexReference {
	private final Value index;

   /**
    * Creates a reference to the variable of the given name
    * @param name the name of the variable being reference by this object
    * @param index the index of the variable being referenced within the array
    */
   public SimpleVariableArrayIndexReference( String name, Value index ) {
 	  super( name );
 	  this.index = index;
   }

   /* 
    * (non-Javadoc)
    * @see org.ldaniels528.javapc.jbasic.values.VariableArrayIndexReference#getIndex()
    */
   public Value getIndex() {
 	  return index;
   }

   /* 
    * (non-Javadoc)
    * @see org.ldaniels528.javapc.jbasic.values.VariableReference#getVariable(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
    */
   public Variable getVariable( final JBasicCompiledCodeReference program ) {
 	   return program.getArrayVariable( this );
   }
   
}
