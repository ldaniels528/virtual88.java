package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.OpCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicProgram;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * NEW Command
 * @author lawrence.daniels@gmail.com
 */
public class NewOp extends GwBasicCommand {

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	   */
	  public NewOp( final TokenIterator it ) 
	  throws JBasicException {
		  JBasicTokenUtil.noMoreTokens( it );
	  }

	  /**
	   * Executes this {@link OpCode opCode}
	   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
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
