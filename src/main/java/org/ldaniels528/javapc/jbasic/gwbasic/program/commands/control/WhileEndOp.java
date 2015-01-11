package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
   * Executes this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
    // check if looping is in order
    //environment.getProgram().conditionalControlIterate( variable );
	  throw new NotYetImplementedException();
  }

}
