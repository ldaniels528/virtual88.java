package jbasic.common.values.systemvariables;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.values.Variable;
import ibmpc.devices.memory.MemoryObject;

/**
 * Represents a JBasic System Variable
 * @author lawrence.daniels@gmail.com
 */
public class JBasicSystemVariable implements Variable {
	  protected final MemoryObject value;
	  protected final String name;
	  
	  //////////////////////////////////////////////////////
	  //    Constructor(s)
	  //////////////////////////////////////////////////////

	  /** 
	   * Creates an instance of this system variable
	   * @param name the name of the system variable
	   */
	  public JBasicSystemVariable( String name, MemoryObject value ) {
		  this.name 	= name;
		  this.value = value;
	  }
	  
	  //////////////////////////////////////////////////////
	  //    Service Method(s)
	  //////////////////////////////////////////////////////
	  
	  /* 
	   * (non-Javadoc)
	   * @see jbasic.values.Variable#getName()
	   */
	  public String getName() {
	    return name;
	  }
	  
	  /* 
	   * (non-Javadoc)
	   * @see ibmpc.program.values.Variable#getValueObject()
	   */
	  public MemoryObject getValueObject() {
		  return value;
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.values.Value#getValue(JBasicEnvironment)
	   */
	  public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		  return value;
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.values.Variable#setValue(jbasic.values.TypedValue)
	   */
	  public void setValue( MemoryObject object ) {
		  value.setContent( object.getContent() );
	  }

	  /**
	   * @return a string representation of this {@link Variable variable}
	   */
	  public String toString() {
		  return new StringBuffer( name ).append( '=' ).append( value ).toString();
	  }
	
}
