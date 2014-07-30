package jbasic.common.values;

import jbasic.common.JBasicCompiledCodeReference;
import ibmpc.devices.memory.MemoryObject;

/**
 * Represents a Value
 */
public interface Value {

  /**
   * Returns the values type containing the value of this element
   * @param program the currently running {@link JBasicCompiledCodeReference program}
   * @return the {@link ibmpc.devices.memory.MemoryObject values type} containing the value of this element
   */
  MemoryObject getValue( JBasicCompiledCodeReference program );
  
}