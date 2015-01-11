package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.console;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

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
	   * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
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
