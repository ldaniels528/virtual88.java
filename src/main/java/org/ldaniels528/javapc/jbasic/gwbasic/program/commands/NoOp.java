package org.ldaniels528.javapc.jbasic.gwbasic.program.commands;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;

/**
 * Represents a Null Operation that does nothing
 */
public class NoOp extends GwBasicCommand {

  /**
   * Creates an instance of this opCode
   */
  public NoOp() {
    // Do nothing
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
    // Do nothing
  }

}
