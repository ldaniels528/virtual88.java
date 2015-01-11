package jbasic.common;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.exceptions.IbmPcException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * Processor for the BASICA/GWBASIC/QBASIC Graphics Macro Language (GML)
 * @author lawrence.daniels@gmail.com
 */
public class JBasicGMLProcessor {
	// define drawing constants
	private static final int ANGLE 		= 0;
	private static final int MOVE_NO_DRAW = 1;
	private static final int COLOR 		= 2;
	private static final int DOWN 		= 3;
	private static final int UP_RIGHT 	= 4;
	private static final int DOWN_RIGHT 	= 5;
	private static final int DOWN_LEFT 	= 6;
	private static final int UP_LEFT 		= 7;
	private static final int LEFT 		= 8;
	private static final int MOVE_ABS 	= 9;
	private static final int DRAW_NO_MOVE = 10;
	private static final int RIGHT 		= 11;
	private static final int SCALE 		= 12;
	private static final int TURN_ANGLE 	= 13;
	private static final int UP 			= 14;
	
	// define relative position constants
	private static final int PLUS = 0;
	private static final int MINUS = 1;
	
	// define the actual drawing commands
	private static final String[] DRAW_OP_STRING = { 
		"A", "B", "C", "D", "E", "F", "G", "H", "L", "M", "N", "R", "S", "T", "U" 
	};
	// create a list of the actual drawing commands
	private static final List<String> DRAWING_OPS = new ArrayList<String>(
			Arrays.asList( DRAW_OP_STRING )
	);
	
	// define the relative movement indicator commands
	private static final String[] RELATIVE_INDICATOR_STRING = {
		"+", "-"
	};
	// create a list of indicators
	private static final List<String> RELATIVE_INDICATOR = new ArrayList<String>(
			Arrays.asList( RELATIVE_INDICATOR_STRING )
	);

	/**
	 * Default constructor
	 */
	public JBasicGMLProcessor() {
		super();
	}
	
	/**
	 * Interprets and Executes the drawing ops
	 * @param screen the given {@link IbmPcDisplay display} to draw to.
	 * @param gmlString the string containing the GML instructions
	 * @throws JBasicException
	 */
	public void draw( final IbmPcDisplay screen, final String gmlString )
	throws JBasicException {
		final TokenIterator it = JBasicTokenUtil.tokenize( gmlString.toUpperCase() );
		try {
			draw( screen, it );
		} 
		catch (IbmPcException e) {
			throw new JBasicException( e );
		}
	}

	/**
	 * Interprets and Executes the drawing ops
	 * @param screen the given {@link IbmPcDisplay display} to draw to.
	 * @param it the given {@link TokenIterator iteration of drawing operations}
	 * @throws JBasicException
	 */
	private void draw( final IbmPcDisplay screen, final TokenIterator it )
	throws IbmPcException {
		// create a context for drawing
		GMLContext ctx = new GMLContext();

		// interpret each element
		while( it.hasNext() ) {
			final String token = nextToken( it );
			int op = DRAWING_OPS.indexOf( token );
			
			switch( op ) {
			case TURN_ANGLE:
				// next token must be [A]ngle
				// if so, drop thru to ANGLE
				JBasicTokenUtil.mandateToken( it, "A" );
				
			case ANGLE:
				ctx.angle = nextInt(it);
				break;

			case COLOR:
				ctx.color = nextInt(it);
				break;

			case DOWN:
				down( ctx, screen, it );
				break;

			case DOWN_LEFT:
				downLeft( ctx, screen, it );
				break;

			case DOWN_RIGHT:
				downRight( ctx, screen, it );
				break;

			case DRAW_NO_MOVE:
				ctx.noMove = true;
				break;

			case LEFT:
				left( ctx, screen, it );
				break;

			case RIGHT:
				right( ctx, screen, it );
				break;

			case MOVE_ABS:
				move( ctx, screen, it );
				break;

			case MOVE_NO_DRAW:
				ctx.noDraw = true;
				break;

			case SCALE:
				ctx.scale = nextInt(it);
				break;

			case UP:
				up( ctx, screen, it );
				break;

			case UP_LEFT:
				upLeft( ctx, screen, it );
				break;

			case UP_RIGHT:
				upRight( ctx, screen, it );
				break;
			}
		}
	}
	
	/**
	 * Moves the cursor down
	 * <br>Syntax: D<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void down( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		screen.drawLine( ctx.x, ctx.y, ctx.x, ctx.y+dist, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.y += dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor diagonally down left
	 * <br>Syntax: G<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void downLeft( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw ) 
			screen.drawLine( ctx.x, ctx.y, ctx.x-dist, ctx.y+dist, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.x -= dist;
			ctx.y += dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor diagonally down right
	 * <br>Syntax: F<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void downRight( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw ) 
			screen.drawLine( ctx.x, ctx.y, ctx.x+dist, ctx.y+dist, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.x += dist;
			ctx.y += dist;
		}
		
		// reset the context
		ctx.reset();
	}

	/**
	 * Moves the cursor to the left
	 * <br>Syntax: L<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void left( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw ) 
			screen.drawLine( ctx.x, ctx.y, ctx.x-dist, ctx.y, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.x -= dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor to an absolute or relative position
	 * <br>Syntax: M[+/-]<i>x</i>,[+/-]<i>y</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void move( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws JBasicException {
		// check for relative indicator for X coordinate
		if( RELATIVE_INDICATOR.contains( it.peekAtNext() ) ) {
			final int indicator = RELATIVE_INDICATOR.indexOf( it.next() );
			switch( indicator ) {
				case PLUS: ctx.x += nextInt(it); break;
				case MINUS: ctx.x -= nextInt(it); break;
			}
		}
		// set the absolute X coordinate
		else {	 				
			ctx.x = nextInt(it);
		}
		
		// expect a comma (,)
		JBasicTokenUtil.mandateToken(it, ",");
		
		// check for relative indicator for Y coordinate
		if( RELATIVE_INDICATOR.contains( it.peekAtNext() ) ) {
			final int indicator = RELATIVE_INDICATOR.indexOf( it.next() );
			switch( indicator ) {
				case PLUS: ctx.y += nextInt(it); break;
				case MINUS: ctx.y -= nextInt(it); break;
			}
		}
		// set the absolute Y coordinate
		else {	 				
			ctx.y = nextInt(it);
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor to the right
	 * <br>Syntax: R<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void right( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw ) 
			screen.drawLine( ctx.x, ctx.y, ctx.x+dist, ctx.y, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.x += dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor up
	 * <br>Syntax: U<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void up( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw )
			screen.drawLine( ctx.x, ctx.y, ctx.x, ctx.y-dist, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.y -= dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor diagonally up left
	 * <br>Syntax: H<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void upLeft( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw )
			screen.drawLine( ctx.x, ctx.y, ctx.x-dist, ctx.y-dist, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.x -= dist;
			ctx.y -= dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Moves the cursor diagonally up right
	 * <br>Syntax: E<i>n</i>
	 * @param ctx the given {@link GMLContext GML context}
	 * @param screen the given {@link IbmPcDisplay display} instance
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	private void upRight( GMLContext ctx, IbmPcDisplay screen, TokenIterator it ) 
	throws IbmPcException {
		// get the distance
		int dist = nextInt(it);
		
		// draw the line
		if( !ctx.noDraw )
			screen.drawLine( ctx.x, ctx.y, ctx.x+dist, ctx.y-dist, ctx.color );
		
		// move the cursor
		if( !ctx.noMove ) {
			ctx.x += dist;
			ctx.y -= dist;
		}
		
		// reset the context
		ctx.reset();
	}
	
	/**
	 * Retrieves the next integer value from the iterator
	 * @param it the given {@link TokenIterator iteration of drawing operations}
	 * @return the integer value
	 * @throws JBasicException
	 */
	private int nextInt(TokenIterator it) throws JBasicException {
		final String token = JBasicTokenUtil.nextToken(it);
		return GwBasicValues.parseIntegerString(token);
	}
	
	/**
	 * Returns the next token
	 * @param it the given {@link TokenIterator iteration of drawing operations}
	 * @return the next token
	 * @throws JBasicException
	 */
	private String nextToken( TokenIterator it ) throws JBasicException {
		// skip semicolons
		while( ";".equals( it.peekAtNext() ) ) {
			it.next();
		}
		
		// return the next valid token
 		return JBasicTokenUtil.nextToken( it );		
	}

	/**
	 * GML Drawing Context 
	 */
	private class GMLContext {
		boolean noDraw;
		boolean noMove;
		int scale;
		int x,y;
		int angle;
		int color;

		/**
		 * Default constructor
		 */
		public GMLContext() {
			this.noDraw 	= false;
			this.noMove 	= false;
			this.scale 	= 1;
			this.x 		= 0;
			this.y 		= 0;
			this.angle 	= 0;
			this.color 	= 1;
		}
		
		/**
		 * Reset this context
		 */
		public void reset() {
			noDraw = false;
			noMove = false;
		}
		
	}
	
}