package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.graphics;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

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
   * Creates an instance of this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
   * @param it the given {@link org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator token iterator}
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
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
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
