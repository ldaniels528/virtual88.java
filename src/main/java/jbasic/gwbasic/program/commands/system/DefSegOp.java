package jbasic.gwbasic.program.commands.system;

import ibmpc.devices.memory.MemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * DEF SEG Command
 * <br>Syntax: DEF SEG[=address]
 * <br>Example: DEF SEG=&HB800
 * @author lawrence.daniels@gmail.com
 */
public class DefSegOp extends GwBasicCommand {
	  private boolean setSegment;
	  private Value segmentVal;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public DefSegOp( TokenIterator it ) throws JBasicException {
		  parse( it );
	  }

	  /**
	   * Executes this {@link jbasic.common.program.OpCode opCode}
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // is this a request to change the current default segment?
		  if( setSegment ) {			  
			  // make sure the variable is an integer
			  final MemoryObject segmentObj = segmentVal.getValue( program );
			  if( !segmentObj.isNumeric() )
				  throw new TypeMismatchException( segmentObj );	
			  
			  // get the unsigned segment value
			  final int segment = GwBasicValues.getUnsignedInt( segmentObj.toInteger() );
			  
			  // set the default segment
			  program.setDefaultMemorySegment( segment );
		  }
		  // otherwise restore default as the program code segment 
		  else {
			  program.setDefaultMemorySegment( program.getCodeSegment() );
		  }
	  }

	  /**
	   * Converts the given textual representation into {@link jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link jbasic.common.tokenizer.TokenIterator iterator}
	   * @throws jbasic.common.exceptions.JBasicException
	   */
	  private void parse( TokenIterator it ) throws JBasicException {
		  // next value must be "SEG"
		  JBasicTokenUtil.mandateToken( it, "SEG" );
		  
		  // if there's another parameter, must be the segment value
		  setSegment = it.hasNext();
		  if( setSegment ) {
			  // must be an equal (=) sign
			  JBasicTokenUtil.mandateToken( it, "=" );
			  
			  // get the segment value
			  segmentVal = GwBasicValues.getValue( it );
			  
			  // if there are anymore tokens, error ...
			  JBasicTokenUtil.noMoreTokens( it );
		  }
	  }
	  
}
