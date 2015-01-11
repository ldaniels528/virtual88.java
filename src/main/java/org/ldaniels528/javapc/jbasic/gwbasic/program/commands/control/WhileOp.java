package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;


import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.ConditionalOpCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * WHILE Command
 * Syntax: WHILE <i>expression<i>
 * @see WhileEndOp
 */
public class WhileOp extends GwBasicCommand implements ConditionalOpCode {
  private final Value expression;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public WhileOp( final TokenIterator it ) 
  throws JBasicException {
      // parse the given expression
      expression = GwBasicValues.getValueReference( it );
      
      // there there are more elements; syntax error
      JBasicTokenUtil.noMoreTokens( it );
  }

  /**
   * Checks to see whether the final condition has been satisified
   * @return true, if the condition has been satified
   */
  public boolean conditionSatisfied( final JBasicCompiledCode compiledCode )
  throws JBasicException {
	  expression.getValue( compiledCode );
	  return false;
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  throw new NotYetImplementedException();
  }

}
