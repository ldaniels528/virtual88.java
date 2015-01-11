package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicRuntime;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicRuntime;
import org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicProgram;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * RUN Command 
 * <br>Purpose: Runs the program
 */
public class RunOp extends GwBasicCommand {

  /**
   * Creates an instance of this opCode
   * @param it the given {@link TokenIterator token iterator}
   * @throws JBasicException
   */
  public RunOp( final TokenIterator it ) 
  throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.common.program.OpCode#execute(org.ldaniels528.javapc.jbasic.common.program.IbmPcProgram)
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  // get the runtime instance
	  final JBasicRuntime runtime = GwBasicRuntime.getInstance();
	  
	  // get the GWBASIC environment
	  final GwBasicEnvironment environment = (GwBasicEnvironment)compiledCode.getSystem();
	  
	  // get the memory resident program
	  final GwBasicProgram program = environment.getProgram();
	  
	  // run the program
	  runtime.run( program );
  }

}
