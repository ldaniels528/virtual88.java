package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.graphics;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * WINDOW Statement
 * <br>Purpose: To draw lines, graphics, and objects in space not 
 * bounded by the physical  limits of the screen.
 * <br>Syntax: WINDOW[[SCREEN](x1,y1)-(x2,y2)
 * @author lawrence.daniels@gmail.com
 */
public class WindowOp extends GwBasicCommand {
	private final Value xpos1;
	private final Value ypos1;
	private final Value xpos2;
	private final Value ypos2;
	
  /**
   * Creates an instance of this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
   * @param it the given {@link org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator token iterator}
   * @throws JBasicException
   */
  public WindowOp( TokenIterator it ) throws JBasicException {
		// check for "SCREEN"
		if( "SCREEN".equals( it.peekAtNext() ) ) {
			it.next();
		}
		
		// mandate the '(' symbol
		JBasicTokenUtil.mandateToken( it, "(" );
		
		// get the x-coordinate
		xpos1 = GwBasicValues.getValueReference( it );
		
		// mandate the ',' symbol
		JBasicTokenUtil.mandateToken( it, "," );
		
		// get the x-coordinate
		ypos1 = GwBasicValues.getValueReference( it );
		
		// mandate the ')' symbol
		JBasicTokenUtil.mandateToken( it, ")" );			
		
		// mandate the '-' symbol
		JBasicTokenUtil.mandateToken( it, "-" );
		
		// mandate the '(' symbol
		JBasicTokenUtil.mandateToken( it, "(" );
		
		// get the radius
		xpos2 = GwBasicValues.getValueReference( it );

		// mandate the "," symbol
		JBasicTokenUtil.mandateToken( it, "," );						
		
		// get xpos2
		ypos2 = GwBasicValues.getValueReference( it );
		
		// mandate the ')' symbol
		JBasicTokenUtil.mandateToken( it, ")" );	
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
	  final int x1 = xpos1.getValue( program ).toInteger();
	  final int y1 = ypos1.getValue( program ).toInteger();
	  final int x2 = xpos2.getValue( program ).toInteger();
	  final int y2 = ypos2.getValue( program ).toInteger();
	  
	  // set the virtual window
	  setVirtualWindow( screen, x1, y1, x2, y2 );
  }
  
  /**
   * Sets the virtual window
 * @throws NotYetImplementedException 
   */
  private void setVirtualWindow( IbmPcDisplay screen, int x1, int y1, int x2, int y2 ) 
  throws NotYetImplementedException {
	  // TODO Figure this one out
	  throw new NotYetImplementedException();
  }

}
