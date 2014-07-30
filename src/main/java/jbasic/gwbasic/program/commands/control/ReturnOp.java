package jbasic.gwbasic.program.commands.control;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.program.commands.system.CallOp;

import com.ldaniels528.tokenizer.TokenIterator;

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
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  // pop from the call stack
	  compiledCode.popFromCallStack();
  }

}
