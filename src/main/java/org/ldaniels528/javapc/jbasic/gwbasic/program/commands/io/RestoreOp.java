package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * Restore Command
 * <br>Syntax: RESTORE
 */
public class RestoreOp extends GwBasicCommand {

  /**
   * Creates an instance of this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public RestoreOp( TokenIterator it ) throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  program.resetDataPointer();
  }

}
