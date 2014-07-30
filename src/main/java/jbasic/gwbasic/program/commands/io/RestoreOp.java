package jbasic.gwbasic.program.commands.io;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * Restore Command
 * <br>Syntax: RESTORE
 */
public class RestoreOp extends GwBasicCommand {

  /**
   * Creates an instance of this {@link jbasic.common.program.OpCode opCode}
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public RestoreOp( TokenIterator it ) throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  program.resetDataPointer();
  }

}
