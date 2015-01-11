package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;

import java.util.LinkedList;
import java.util.List;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.impl.SimpleConstant;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.NoOp;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.PreProcessedOpCode;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * DATA Command 
 * <br>Syntax: DATA <i>constant1</i>[,<i>constant2</i>[,<i>constant3</i>]]
 */
public class DataOp extends NoOp implements PreProcessedOpCode {
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
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( final JBasicCompiledCode compiledCode ) {
	  // Do nothing this opCode gets pre-processed
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.gwbasic.program.opcodes.PreprocessibleOpCode#preProcess(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
   */
  public void preProcess(final JBasicCompiledCode compiledCode) {
	  // add the values to the given program
	  for( final SimpleConstant constant : constants ) {
		  compiledCode.addData( constant );
	  }
  }

  /**
   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
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
