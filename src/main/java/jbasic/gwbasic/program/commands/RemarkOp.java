package jbasic.gwbasic.program.commands;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;

/**
 * Remark Command (REM)
 */
public class RemarkOp extends GwBasicCommand {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public RemarkOp( TokenIterator it ) throws JBasicException {
    // Do nothing
  }

  /**
   * Executes this {@link jbasic.common.program.OpCode opCode}
 * @throws jbasic.common.exceptions.JBasicException
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
    // Do nothing... it's just a remark ;)
  }
  
  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  return "REM";
  }

}
