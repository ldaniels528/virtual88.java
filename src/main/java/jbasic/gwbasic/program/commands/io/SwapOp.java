package jbasic.gwbasic.program.commands.io;

import ibmpc.devices.memory.MemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableReference;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

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
	   * Executes this {@link jbasic.common.program.OpCode opCode}
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
	   * Converts the given textual representation into {@link jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link TokenIterator iterator}
	   * @throws jbasic.common.exceptions.JBasicException
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
