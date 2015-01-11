package org.ldaniels528.javapc.jbasic.common.values;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

/**
 * Represents a Value
 */
public interface Value {

  /**
   * Returns the values type containing the value of this element
   * @param program the currently running {@link JBasicCompiledCodeReference program}
   * @return the {@link org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject values type} containing the value of this element
   */
  MemoryObject getValue( JBasicCompiledCodeReference program );
  
}