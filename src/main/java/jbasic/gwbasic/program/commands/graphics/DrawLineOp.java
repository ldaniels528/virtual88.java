package jbasic.gwbasic.program.commands.graphics;

import ibmpc.devices.display.IbmPcDisplay;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * LINE Command
 * Syntax: LINE [(x1,y1)]-(x2,y2) [,[attribute][,B[F]][,style]]
 * @author lawrence.daniels@gmail.com
 */
public class DrawLineOp extends GwBasicCommand {	
	private final Value xpos2;
	private final Value ypos2;
	private Value xpos1;
	private Value ypos1;
	private Value attribute;
	private Value style;
	private boolean box;
	private boolean filledBox;
	
  /**
   * Creates an instance of this {@link jbasic.common.program.OpCode opCode}
   * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator token iterator}
   * @throws JBasicException
   */
  public DrawLineOp( TokenIterator it ) throws JBasicException {
		// read the first token ...
		final String firstToken = it.next();
		
		// if it's an opening parenthesis, it's a full statement ...
		if( "(".equals( firstToken ) ) {
			// get the x-coordinate
			xpos1 = GwBasicValues.getValueReference( it );
			
			// mandate the ',' symbol
			JBasicTokenUtil.mandateToken( it, "," );
			
			// get the x-coordinate
			ypos1 = GwBasicValues.getValueReference( it );
			
			// mandate the ')' symbol
			JBasicTokenUtil.mandateToken( it, ")" );
		}			
		
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
		
		// if there is another parameter, attempt to read it
		if( it.hasNext() ) {			
			// get the attribute
			JBasicTokenUtil.mandateToken( it, "," );			
			attribute = GwBasicValues.getValueReference( it );
			
			// attempt to get the start element
			if( it.hasNext() ) {
				// get the box/box filled value
				JBasicTokenUtil.mandateToken( it, "," );				
				String s = it.next();
				if( "B".equalsIgnoreCase( s ) ) box = true;
				else if( "BF".equalsIgnoreCase( s ) ) filledBox = true;
				else throw new JBasicException( "Illegal identifier; Expected 'B 'or 'BF'" );
					
				// attempt to get the f value
				if( it.hasNext() ) {
					JBasicTokenUtil.mandateToken( it, "," );
					style = GwBasicValues.getValueReference( it );
					
					// if any more tokens, error ...
					JBasicTokenUtil.noMoreTokens( it );					
				}				
			}
		}    
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.environment.JBasicEnvironment)
   */
  public void execute( final JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the display instance
	  final IbmPcDisplay display = program.getSystem().getDisplay();
	  
	  // get mandatory parameters
	  final int x2 = xpos2.getValue( program ).toInteger();
	  final int y2 = ypos2.getValue( program ).toInteger();
	  
	  // get optional parameters
	  final int x1 = ( xpos1 != null ) ? xpos1.getValue( program ).toInteger() : 0;
	  final int y1 = ( ypos1 != null ) ? ypos1.getValue( program ).toInteger() : 0;	  	  
	  final int a = ( attribute != null ) ? attribute.getValue( program ).toInteger() : 0;
	  final int s = ( style != null ) ? style.getValue( program ).toInteger() : 0;
	  
	  // draw the line, rectangle, or filled rectangle
	  drawShape( display, x1, y1, x2, y2, a, s );
  }
  
  /**
   * Draw the shape (line, rectangle, or filled rectangle) 
   * at (x1,y1) having a length of x2 and a height of y2
   * @param x1 the given x position of the rectangle
   * @param y1 the given y position of the rectangle
   * @param x2 the given width of the rectangle
   * @param y2 the given height of the rectangle
   * @param attr the given color attribute
   * @param style the given style attribute
   */
  private void drawShape(	final IbmPcDisplay display, 
		  				final int x1, 
		  				final int y1, 
		  				final int x2, 
		  				final int y2, 
		  				final int attr,  
		  				final int style ) {
	  // draw an empty rectangle
	  if( box ) {
		 drawBox( display, x1, y1, x2, y2, attr );
	  }
	  // draw a filled rectangle
	  else if( filledBox ) {
		  drawFilledBox( display, x1, y1, x2, y2, attr );
	  }
	  // draw a line
	  else {
		  display.drawLine( x1, y1, x2, y2, attr );
	  }
  }
  
  /**
   * Draws a rectangle at (x1,y1) having a length of x2 and a height of y2
   * @param display the given {@link IbmPcDisplay display}
   * @param x1 the given x position of the rectangle
   * @param y1 the given y position of the rectangle
   * @param x2 the given width of the rectangle
   * @param y2 the given height of the rectangle
   * @param attr the given color attribute
   */
  private void drawBox( final IbmPcDisplay display, 
					   final int x1, 
					   final int y1, 
					   final int x2, 
					   final int y2, 
					   final int attr ) {
	  // top
	  display.drawLine( x1, y1, x2, y1, attr );
	  // left side
	  display.drawLine( x1, y1, x1, y2, attr );
	  // right side
	  display.drawLine( x2, y1, x2, y2, attr );
	  // bottom
	  display.drawLine( x1, y2, x2, y2, attr );
  }
  
  /**
   * Draws a filled rectangle at (x1,y1) having a length of x2 and a height of y2
   * @param display the given {@link IbmPcDisplay display}
   * @param x1 the given x position of the rectangle
   * @param y1 the given y position of the rectangle
   * @param x2 the given width of the rectangle
   * @param y2 the given height of the rectangle
   * @param attr the given color attribute
   */
  private void drawFilledBox( final IbmPcDisplay display, 
					   		final int x1, 
					   		final int y1, 
					   		final int x2, 
					   		final int y2, 
					   		final int attr ) {
	  for( int y = y1; y < y2; y++ ) {
		  display.drawLine( x1, y, x2, y, attr );
	  }
  }

}
