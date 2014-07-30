package jbasic.gwbasic.program.commands.control;


import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.ConditionalOpCode;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

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
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  throw new NotYetImplementedException();
  }

}
