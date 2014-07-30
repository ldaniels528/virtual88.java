package jbasic.gwbasic.program.commands.io;

import java.util.LinkedList;
import java.util.List;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.impl.SimpleConstant;
import jbasic.gwbasic.program.commands.NoOp;
import jbasic.gwbasic.program.commands.PreprocessibleOpCode;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * DATA Command 
 * <br>Syntax: DATA <i>constant1</i>[,<i>constant2</i>[,<i>constant3</i>]]
 */
public class DataOp extends NoOp implements PreprocessibleOpCode {
  private final List<SimpleConstant> constants;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public DataOp( final TokenIterator it ) 
  throws JBasicException {
	  this.constants = parse( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( final JBasicCompiledCode compiledCode ) {
	  // Do nothing this opCode gets pre-processed
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.gwbasic.program.opcodes.PreprocessibleOpCode#preprocess(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
   */
  public void preprocess( final JBasicCompiledCode compiledCode ) {
	  // add the values to the given program
	  for( final SimpleConstant constant : constants ) {
		  compiledCode.addData( constant );
	  }
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws jbasic.common.exceptions.JBasicException
   */
  private List<SimpleConstant> parse( final TokenIterator it ) 
  throws JBasicException {
	  final List<SimpleConstant> constants = new LinkedList<SimpleConstant>();
	  while( it.hasNext() ) {
		  // add value
		  constants.add( GwBasicValues.getConstantValue( it ) );

		  // there there are more elements; check for a values separator (comma)
		  if( it.hasNext() ) JBasicTokenUtil.mandateToken( it, "," );
	  }
	  return constants;
  }

}
