package jbasic.gwbasic.program.commands.control;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.JBasicRuntime;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.GwBasicRuntime;
import jbasic.gwbasic.program.GwBasicProgram;
import jbasic.gwbasic.program.commands.GwBasicCommand;

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
   * @see jbasic.common.program.OpCode#execute(jbasic.common.program.IbmPcProgram)
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
