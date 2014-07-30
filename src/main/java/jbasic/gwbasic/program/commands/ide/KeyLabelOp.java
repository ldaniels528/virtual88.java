package jbasic.gwbasic.program.commands.ide;

import ibmpc.devices.memory.MemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

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
	   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
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
	   * Converts the given textual representation into {@link jbasic.common.values.Value values}
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
