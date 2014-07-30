package jbasic.gwbasic.program.commands;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;

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
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
    // Do nothing
  }

}
