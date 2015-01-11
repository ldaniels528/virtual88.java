package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * RESUME Statement
 * Syntax: RESUME [label/lineNumber]
 */
public class ResumeOp extends GwBasicCommand {
	  private final String label;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	   */
	  public ResumeOp( TokenIterator it ) throws JBasicException {
		    this.label = JBasicTokenUtil.nextToken( it ); 
		    JBasicTokenUtil.noMoreTokens( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // goto the label
		  compiledCode.gotoLabel( label, false );
	  }

}
