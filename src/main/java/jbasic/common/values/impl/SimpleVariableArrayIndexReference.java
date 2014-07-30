package jbasic.common.values.impl;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.values.Value;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableArrayIndexReference;

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
    * @see jbasic.values.VariableArrayIndexReference#getIndex()
    */
   public Value getIndex() {
 	  return index;
   }

   /* 
    * (non-Javadoc)
    * @see jbasic.values.VariableReference#getVariable(jbasic.environment.JBasicEnvironment)
    */
   public Variable getVariable( final JBasicCompiledCodeReference program ) {
 	   return program.getArrayVariable( this );
   }
   
}
