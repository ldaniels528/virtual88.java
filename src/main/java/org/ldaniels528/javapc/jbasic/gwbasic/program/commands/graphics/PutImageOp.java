package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.graphics;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * PUT Command
 * <br>Syntax: PUT (<i>x</i>,<i>y</i>),<i>imageName</i>,<i>imageOp</i>)
 * @author lawrence.daniels@gmail.com
 */
public class PutImageOp extends GwBasicCommand {
	private final Value xpos;
	private final Value ypos;
	private final Value image;
	private final String imageOp;

	  /**
	   * Creates an instance of this operation
	   * @param it the given {@link TokenIterator token iterator}
	   * @throws JBasicException
	   */
	  public PutImageOp( TokenIterator it ) throws JBasicException {  
		  // mandate the '(' symbol
		  JBasicTokenUtil.mandateToken( it, "(" );
			
		  // get the x-coordinate
		  xpos = GwBasicValues.getValueReference( it );
			
		  // mandate the ',' symbol
		  JBasicTokenUtil.mandateToken( it, "," );
			
		  // get the x-coordinate
		  ypos = GwBasicValues.getValueReference( it );
			
		  // mandate the ')' symbol
		  JBasicTokenUtil.mandateToken( it, ")" );			

		  // mandate the ',' symbol
		  JBasicTokenUtil.mandateToken( it, "," );
		  
		  // get the draw ops
		  image = GwBasicValues.getValue( it );
		  
		  // mandate the ',' symbol
		  JBasicTokenUtil.mandateToken( it, "," );
		  
		  // get the draw option
		  imageOp = JBasicTokenUtil.nextToken( it );
		  
		  // no more tokens
		  JBasicTokenUtil.noMoreTokens( it );	
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // get the screen instance
		  final IbmPcDisplay screen = program.getSystem().getDisplay();
		  
		  // if current mode is not graphical, error
		  final IbmPcDisplayMode mode = screen.getDisplayMode();
		  if( !mode.isGraphical() )
			  throw new IllegalFunctionCallException( this );
		  
		  // get parameters		  
		  final int x = xpos.getValue( program ).toInteger();
		  final int y = ypos.getValue( program ).toInteger();
		  
		  // make sure that the value is a bitmap
		  final MemoryObject bitMap = image.getValue( program );
		  
		  // interpret the draw ops
		  drawImage( bitMap, x, y, imageOp );			  
	  }

	  /**
	   * Interprets and Executes the drawing ops
	   * @param drawOps the given draw operations
	   */
	  private void drawImage( final MemoryObject bitMap, 
			  				 final int x, 
			  				 final int y,
			  				 final String imageOp ) 
	  throws JBasicException {
		  throw new NotYetImplementedException();
	  }

}
