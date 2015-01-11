package org.ldaniels528.javapc.jbasic.common.values.impl;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

/**
 * Represents a JBasic Constant 
 */
public class SimpleConstant implements Value {
  private final MemoryObject value;

  //////////////////////////////////////////////////////
  //    Constructor(s)
  //////////////////////////////////////////////////////

  /**
   * Creates an instance of a JBasic constant
   * @param value the constant value
   */
  public SimpleConstant( final MemoryObject value ) {
	  this.value = value;
  }

  //////////////////////////////////////////////////////
  //    Service Method(s)
  //////////////////////////////////////////////////////
  
  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(JBasicEnvironment)
   */
  public MemoryObject getValue( JBasicCompiledCodeReference program ) {
	  return value;
  }
  
  /* 
   * (non-Javadoc)
   * @see Object#toString()
   */
  public String toString() {
	  return value.toString();
  }

}
