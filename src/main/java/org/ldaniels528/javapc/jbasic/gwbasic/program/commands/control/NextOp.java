package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.program.ConditionalControlBlock;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * NEXT Command
 * <br>Syntax:  NEXT [<i>variable</i>]
 * <br>Example: NEXT I
 * @see ConditionalControlBlock
 * @see ForOp
 */
public class NextOp extends GwBasicCommand {
  private VariableReference reference;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public NextOp( final TokenIterator it ) 
  throws JBasicException {
	  if( it.hasNext() ) {
		  // get a reference to the variable
	      reference = GwBasicValues.getVariableReference( it );
	
	      // there should be no more tokens
	      JBasicTokenUtil.noMoreTokens( it );
	  }
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.ibmpc.program.OpCode#execute(org.ldaniels528.javapc.ibmpc.program.IbmPcProgram, org.ldaniels528.javapc.ibmpc.IbmPcEnvironment)
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  final Variable variable;
	  
	  // if a reference was past, use it ...
	  if( reference != null ) {
		  // lookup the control variable
		  variable = reference.getVariable( compiledCode );
	  }
	  
	  // otherwise, make an educated guess ...
	  else {
		  variable = compiledCode.getLastControlStackVariable();
		  if( variable == null )
			  throw new SyntaxErrorException();
	  }

	  // check if looping is in order
	  compiledCode.conditionalControlIterate( compiledCode, variable );
  }
  
  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  return "NEXT";
  }

}
