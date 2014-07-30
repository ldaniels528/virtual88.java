package jbasic.common.program;

import jbasic.common.exceptions.JBasicException;

/**
 * Represents a {@link OpCode Conditional OpCode}
 */
public interface ConditionalOpCode {

  /**
   * Checks to see whether the final condition has been satisified
   * @param compiledCode the currently running {@link JBasicSourceCode compiled code}
   * @return true, if the condition has been satified
   */
  boolean conditionSatisfied( JBasicCompiledCode compiledCode )
  throws JBasicException;

}
