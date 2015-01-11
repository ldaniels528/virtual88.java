package jbasic.gwbasic.program.commands.control;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * While End Command
 * Syntax: WEND
 * @see WhileOp
 */
public class WhileEndOp extends GwBasicCommand {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public WhileEndOp( TokenIterator it ) throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /**
   * Executes this {@link jbasic.common.program.OpCode opCode}
 * @throws jbasic.common.exceptions.JBasicException
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
    // check if looping is in order
    //environment.getProgram().conditionalControlIterate( variable );
	  throw new NotYetImplementedException();
  }

}
