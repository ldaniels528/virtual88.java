package jbasic.gwbasic.program.commands;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.OpCode;

/**
 * Represents an {@link OpCode opCode} that should be pre-processed
 */
public interface PreprocessibleOpCode extends OpCode {

  /**
   * Preprocesses this {@link OpCode opCode}
   * @param compiledCode the current executing {@link JBasicCompiledCode compiled code}
   */
  void preprocess( JBasicCompiledCode compiledCode ) 
  throws JBasicException;

}
