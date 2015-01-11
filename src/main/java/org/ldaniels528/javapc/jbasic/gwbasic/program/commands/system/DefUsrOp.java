package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * DEF USR Command
 * <br>Syntax: DEF USR<i>n</i>=<i>address</i>
 * <br>Example: DEF USR0=&HF500
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.DefSegOp
 * @see org.ldaniels528.javapc.jbasic.gwbasic.functions.system.UsrFunction
 * @author lawrence.daniels@gmail.com
 */
public class DefUsrOp extends GwBasicCommand {
	  private final Value offsetValue;
	  private final int functionNumber;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public DefUsrOp( TokenIterator it ) throws JBasicException {
		  // get the next token "USRx"
		  final String token = JBasicTokenUtil.nextToken( it );
		  if( token.length() < 4 )
			  throw new SyntaxErrorException();
		  
		  // extract the index portion "USR0" => "0"
		  final String indexStr = token.substring( 3 );
		  if( !GwBasicValues.isIntegerConstant( indexStr ) )
			  throw new SyntaxErrorException();
		  
		  // get the function number
		  functionNumber = GwBasicValues.parseIntegerString( indexStr );
		  
		  // must be an equal (=) sign
		  JBasicTokenUtil.mandateToken( it, "=" );
		  
		  // get the segment value
		  offsetValue = GwBasicValues.getValue( it );
		  
		  // if there are anymore tokens, error ...
		  JBasicTokenUtil.noMoreTokens( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // get the offset value object
		  MemoryObject object = offsetValue.getValue( program );
		  if( !object.isNumeric() )
			  throw new TypeMismatchException( object );
		  
		  // get segment and offset of the code to execute
		  final int segment = program.getDefaultMemorySegment();
		  final int offset = object.toInteger();

		  // add the information about the user defined assembly rountine to the program
		  // TODO figure this out		  
	  }

}
