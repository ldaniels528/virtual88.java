package jbasic.gwbasic.program.commands.graphics;

import jbasic.common.tokenizer.TokenIterator;
import ibmpc.devices.display.IbmPcDisplay;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;
import org.apache.log4j.Logger;

import static java.lang.String.format;

/**
 * CIRCLE Command
 * <br>Syntax: CIRCLE(xcenter, ycenter),radius[,[color][,[start],[end][,aspect]]]
 * @author lawrence.daniels@gmail.com
 */
public class DrawCircleOp extends GwBasicCommand {
	private final Logger logger = Logger.getLogger(getClass());
	private static final int NPOINTS = 36;
	private static final double DIVISION = Math.PI / NPOINTS;
	private final Value xpos;
	private final Value ypos;
	private final Value radius;
	private Value color;
	private Value start;
	private Value end;
	private Value aspect;
	
  /**
   * Creates an instance of this {@link jbasic.common.program.OpCode opCode}
   * @param it the given {@link jbasic.common.tokenizer.TokenIterator token iterator}
   * @throws JBasicException
   */
  public DrawCircleOp( final TokenIterator it ) throws JBasicException {
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
		
		// mandate the 'm' symbol
		JBasicTokenUtil.mandateToken( it, "," );
		
		// get the radius
		radius = GwBasicValues.getValueReference( it );
			
		// if there is another parameter, attempt to read it
		if( it.hasNext() ) {			
			// get the color
			JBasicTokenUtil.mandateToken( it, "," );						
			color = GwBasicValues.getValueReference( it );
			
			// attempt to get the start element
			if( it.hasNext() ) {
				JBasicTokenUtil.mandateToken( it, "," );
				start = GwBasicValues.getValueReference( it );
				
				// attempt to get the end element
				if( it.hasNext() ) {
					JBasicTokenUtil.mandateToken( it, "," );
					end = GwBasicValues.getValueReference( it );
					
					// attempt to get the aspect element
					if( it.hasNext() ) {
						JBasicTokenUtil.mandateToken( it, "," );
						aspect = GwBasicValues.getValueReference( it );
					
						// if any more tokens, error ...
						JBasicTokenUtil.noMoreTokens( it );
					}
				}
			}
		}    		
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(JBasicSourceCode)
   */
  public void execute( final JBasicCompiledCode program ) 
  throws JBasicException {	 
	  // get the display instance
	  final IbmPcDisplay display = program.getSystem().getDisplay();
	  
	  // get mandatory parameters
	  final int x = xpos.getValue( program ).toInteger();
	  final int y = ypos.getValue( program ).toInteger();
	  final int r = radius.getValue( program ).toInteger();
	  
	  // get optional parameters
	  final Integer c = ( color != null ) ? color.getValue(program).toInteger() : null;
	  final Integer s = ( start != null ) ? start.getValue(program).toInteger() : null;
	  final Integer e = ( end != null ) ? end.getValue(program).toInteger() : null;
	  final Integer a = ( aspect != null ) ? aspect.getValue(program).toInteger() : null;
	  
	  // draw the circle
	  drawCircle( display, x, y, r, c, s, e, a );
  }
  
  /**
   * Draws a circle at coordinates (x,y) with a radius of r and a color of c.
   * @param display the given {@link IbmPcDisplay display} to draw the graphics to.
   * @param x the given x-coordinate of the center of the circle
   * @param y the given y-coordinate of the center of the circle
   * @param radius the given radius
   * @param color the given color index
   * @param start the given start position
   * @param end the given ending position
   * @param aspect the given aspect
   */
  private void drawCircle( final IbmPcDisplay display, 
		  				  final int x, final int y, 
		  				  final int radius, 
		  				  final Integer color, 
		  				  final Integer start, 
		  				  final Integer end, 
		  				  final Integer aspect ) {
	  logger.debug(format("circle at (%d,%d) where r = %d, c = %s, s = %s, e = %s, a = %s", x,y,radius,color,start,end,aspect));
	  for( double n = -Math.PI; n <= Math.PI; n += DIVISION ) {
		  // compute the "from" point
		  final double ax = Math.sin( n ) * radius + x;
		  final double ay = Math.cos( n ) * radius + y;
		  
		  // compute the "to" point
		  final double m  = n + DIVISION;
		  final double bx = Math.sin( m ) * radius + x;
		  final double by = Math.cos( m ) * radius + y;
		  
		  // draw the line segment
		  display.drawLine( (int)ax, (int)ay, (int)bx, (int)by, color );
	  }
  }

}
