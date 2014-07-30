package jbasic.gwbasic.program.commands;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;

/**
 * BEEP Command
 * <br>Syntax: BEEP
 */
public class BeepOp extends GwBasicCommand {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public BeepOp( TokenIterator it ) throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  throw new NotYetImplementedException();
  }

}
