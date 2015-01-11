package jbasic.gwbasic.program.commands.graphics;

import jbasic.common.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/** 
 * PRESET and PSET Statements
 * <br>Purpose: To display a point at a specified place on the screen 
 * during use of the graphics mode.
 * <br>Syntax1: PRESET(x,y)[,color]
 * <br>Syntax2: PSET(x,y)[,color
 * @author lawrence.daniels@gmail.com
 */
public class PreSetOp extends GwBasicCommand {
	private final Value xpos;
	private final Value ypos;
	private Value color;	
	
  /**
   * Creates an instance of this {@link jbasic.common.program.OpCode opCode}
   * @param it the given {@link jbasic.common.tokenizer.TokenIterator token iterator}
   * @throws JBasicException
   */
  public PreSetOp( TokenIterator it ) throws JBasicException {
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
		
		// is the color specified?
		if( it.hasNext() ) {
			// mandate the '-' symbol
			JBasicTokenUtil.mandateToken( it, "," );
					
			// get the radius
			color = GwBasicValues.getValueReference( it );
		}

		// no more tokens are allowed
		JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.environment.JBasicEnvironment)
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
	  final int c = ( color != null ) ? color.getValue( program ).toInteger() : -1;
	  
	  // draw the pixel
	  try {
		screen.drawLine( x, y, x, y, c );
	  } 
	  catch( RuntimeException e ) {
		throw new JBasicException( e );
	  }
  }
  
}
