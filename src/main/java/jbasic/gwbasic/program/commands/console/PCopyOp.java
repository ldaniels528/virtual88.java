package jbasic.gwbasic.program.commands.console;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;

import jbasic.common.tokenizer.TokenIterator;

/** 
 * PCOPY Command
 * <br>Purpose: To copy one screen page to another in all screen modes.
 * <br>Syntax: PCOPY <i>sourcePage</i>, <i>destinationPage</i>
 * <br>Example: PCOPY 1,2
 * @author lawrence.daniels@gmail.com
 */
public class PCopyOp extends GwBasicCommand {
	  private final Value sourcePage;
	  private final Value destinationPage;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public PCopyOp( TokenIterator it ) throws JBasicException {
		  // parse all the values
		  final Value[] values = JBasicTokenUtil.parseValues( it, 2, 2 );
		  
		  // identify our values for use
	      sourcePage 	= values[0];
	      destinationPage = values[1];
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {		  
		  // get the display instance
		  final IbmPcDisplay screen = program.getSystem().getDisplay();
		  
		  // get the source and destination pages
		  final MemoryObject srcPage = sourcePage.getValue( program );
		  if( !srcPage.isNumeric() )
			  throw new TypeMismatchException( srcPage );
		  
		  final MemoryObject dstPage = destinationPage.getValue( program );
		  if( !dstPage.isNumeric() )
			  throw new TypeMismatchException( dstPage );
		  
		  // copy video page
		  screen.copyPage( srcPage.toInteger() - 1, dstPage.toInteger() - 1 );
	  }

}
