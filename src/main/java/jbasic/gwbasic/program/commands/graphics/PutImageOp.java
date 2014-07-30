package jbasic.gwbasic.program.commands.graphics;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

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
	   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
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
