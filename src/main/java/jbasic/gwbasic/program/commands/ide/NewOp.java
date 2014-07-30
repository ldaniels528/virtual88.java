package jbasic.gwbasic.program.commands.ide;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.OpCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.GwBasicProgram;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * NEW Command
 * @author lawrence.daniels@gmail.com
 */
public class NewOp extends GwBasicCommand {

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws jbasic.common.exceptions.JBasicException
	   */
	  public NewOp( final TokenIterator it ) 
	  throws JBasicException {
		  JBasicTokenUtil.noMoreTokens( it );
	  }

	  /**
	   * Executes this {@link OpCode opCode}
	   * @throws jbasic.common.exceptions.JBasicException
	   */
	  public void execute( final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // get the GWBASIC environment
		  final GwBasicEnvironment environment = (GwBasicEnvironment)compiledCode.getSystem();
		  
		  // get the memory resident program
		  final GwBasicProgram program = environment.getProgram();
		  
		  // clear the program
		  program.clear();
	  }

}
