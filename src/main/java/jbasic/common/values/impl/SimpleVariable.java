package jbasic.common.values.impl;

import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.values.Variable;

/**
 * Represents a BASICA/GWBASIC/QBASIC Variable
 */
public class SimpleVariable implements Variable {
	private final MemoryObject value;
	private final String name;
  
  //////////////////////////////////////////////////////
  //    Constructor(s)
  //////////////////////////////////////////////////////

  /**
   * Creates an instance of a {@link Variable variable}
   * @param name the given name of the variable
   * @param value the given {@link MemoryObject memory object}
   */
  public SimpleVariable( String name, MemoryObject value ) {
    this.name	= name;
    this.value	= value;
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
  
  /**
   * Deallocates memory being used by this variable
   * @throws JBasicException 
   */
  public void destroy() {
	  value.destroy();
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
	  return name;
  }

}
