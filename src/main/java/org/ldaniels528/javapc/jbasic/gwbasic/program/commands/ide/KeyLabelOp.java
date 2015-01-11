package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * KEY Command
 * <br>Syntax: KEY n,CHR$(hex code)+CHR$(scan code)
 * @author lawrence.daniels@gmail.com
 *
 */
public class KeyLabelOp extends GwBasicCommand {
	  private Value keyIndex;
	  private Value keyLabel;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public KeyLabelOp( TokenIterator it ) throws JBasicException {
		  parse( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // key index must be numeric
		  final MemoryObject key = keyIndex.getValue( program );
		  if( !key.isNumeric() ) 
			  throw new TypeMismatchException( key );
		  
		  // key label must be a string
		  final MemoryObject label = keyLabel.getValue( program ); 
		  if( !label.isString() )
			  throw new TypeMismatchException( label );
		  
		  // get the key index
		  final int index = key.toInteger();
		  if( index < 1 || index > 10 )
			  throw new JBasicException( "Illegal value used" );
		  
		  // set the label
		  final GwBasicEnvironment gwBasicEnvironment = (GwBasicEnvironment)program.getSystem();
		  gwBasicEnvironment.setKeyLabel( index, label.toString().toLowerCase() );
	  }

	  /**
	   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link TokenIterator iterator}
	   * @throws JBasicException
	   */
	  private void parse( TokenIterator it ) throws JBasicException {
		  // get the key index
		  keyIndex = GwBasicValues.getValue( it ); 

		  // next must be comma
		  JBasicTokenUtil.mandateToken( it, "," );
		  
		  // get the key label
		  keyLabel = GwBasicValues.getValue( it );
		  
		  // there should be no more tokens
		  JBasicTokenUtil.noMoreTokens( it );
	  }

}
