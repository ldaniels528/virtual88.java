package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * SWAP Command
 * Syntax: SWAP <variable1>, <variable2>
 * @author lawrence.daniels@gmail.com
 */
public class SwapOp extends GwBasicCommand {
	  private VariableReference variableRef1;
	  private VariableReference variableRef2;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public SwapOp( TokenIterator it ) throws JBasicException {
		  parse( it );
	  }

	  /**
	   * Executes this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
	   * @throws JBasicException
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // get the values for each
		  MemoryObject value1 = variableRef1.getValue( program );
		  MemoryObject value2 = variableRef2.getValue( program );
		  
		  // make sure types are compatible
		  if( !value1.isCompatibleWith( value2 ) )
			  throw new TypeMismatchException( value2 );
			  		  
		  // get the variables
		  Variable variable1 = variableRef1.getVariable( program );
		  Variable variable2 = variableRef2.getVariable( program );
		  
		  // swap the values
		  variable1.setValue( value2 );
		  variable2.setValue( value1 );
	  }

	  /**
	   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link TokenIterator iterator}
	   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	   */
	  private void parse( TokenIterator it ) throws JBasicException {
		// get the first variable reference
		variableRef1 = GwBasicValues.getVariableReference( it );
		
		// there there are more elements; check for a command separator (comma)
		JBasicTokenUtil.mandateToken( it, "," );
		
		// get the first variable reference
		variableRef2 = GwBasicValues.getVariableReference( it );
	  }

}
