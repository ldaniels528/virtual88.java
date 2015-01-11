package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.CallOp;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * RETURN Command
 * <br>Purpose: Returns from subroutine
 * <br>Syntax: RETURN
 * @see CallOp
 * @author lawrence.daniels@gmail.com
 */
public class ReturnOp extends GwBasicCommand {

  /**
   * Default Constructor
   * @param it the given {@link TokenIterator token iterator}
   * @throws JBasicException
   */	
  public ReturnOp( TokenIterator it ) throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  // pop from the call stack
	  compiledCode.popFromCallStack();
  }

}
