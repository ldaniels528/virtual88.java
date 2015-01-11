package org.ldaniels528.javapc.ibmpc.devices.display.modes;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.apache.log4j.Logger;

import static java.lang.String.format;

/**
 * Represents a Generic Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractGraphicsMode extends AbstractDisplayMode {
	protected static final byte CLEAR_BYTE = 0x00;
	private final Logger logger = Logger.getLogger(getClass());
	
	/////////////////////////////////////////////////////
	//      Constructor(s)
	/////////////////////////////////////////////////////

	/**
	 * Creates an instance of this graphics mode
	 * @param videoMode the given video mode
	 * @param memorySegment the given video memory segment
	 * @param columns the given column width
	 * @param rows the given row height
	 * @param width the given graphics width
	 * @param height the given graphics height
	 * @param fontWidth the given font width
	 * @param fontHeight the given font height
	 * @param colors the number of colors this mode can display
	 * @param pixelsPerByte the of pixels represented by each byte in video memory
	 * @param memorySize the size of video memory
	 */
	public AbstractGraphicsMode(	int videoMode, 
								int memorySegment, 
								int columns, int rows, 
								int width, int height, 
								int fontWidth, int fontHeight, 
								int colors, 
								int pixelsPerByte, 
								int memorySize ) {
		super( videoMode, memorySegment, columns, rows, width, height, fontWidth,
			   fontHeight, colors, rows*columns/pixelsPerByte, memorySize, true );
	}
	
	/////////////////////////////////////////////////////
	//      Service Method(s)
	/////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#backspace(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext)
	 */
	public void backspace( IbmPcDisplayContext dc ) {
		if( dc.position > 0 ) {
			// go back 1 position
			dc.position--;
			
			// calculate the position in pixels as (x,y)
			final int x1 = ( dc.position % columns ) * fontWidth;
			final int x2 = x1 + fontWidth;
			final int y1 = ( dc.position / columns ) * fontHeight;
			final int y2 = y1 + fontHeight;
			
			// clear the rectangle
			for( int y = y1; y < y2; y++ ) {
				drawHorizontalLine( dc, x1, y, x2, y, 0 );
			}
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#clear(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext)
	 */
	public void clear( IbmPcDisplayContext dc ) {
		// if multiple pages are available ...
		if( pages > 1 ) {
			final int offset = dc.activePage * pageSize;
			dc.memory.fill( memorySegment, offset, pageSize, CLEAR_BYTE );
		}
		// must be single page, clear entire memory
		else {
			dc.memory.fill( memorySegment, 0, memorySize, CLEAR_BYTE );
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#drawLine(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int, int, int)
	 */
	public void drawLine( IbmPcDisplayContext dc, int x1, int y1, int x2, int y2, int colorIndex ) {		
		// is the line vertical?
		if( x1 == x2 ) {
			drawVerticalLine( dc, x1, y1, x2, y2, colorIndex );
		}
		// is the line horizontal?
		else if( y1 == y2 ) {
			drawHorizontalLine( dc, x1, y1, x2, y2, colorIndex );
		}
		// must be an angled line ...
		else {			
			drawAngledLine( dc, x1, y1, x2, y2, colorIndex );
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#getCursorPosition(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public int getCursorPosition( IbmPcDisplayContext dc, int column, int row ) {
		final int pageOffset = dc.activePage * pageSize;
		return pageOffset + ( ( row - 1 ) * columns ) + ( column - 1 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#newLine(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext)
	 */
	public void newLine( IbmPcDisplayContext dc ) {
	    // goto column 1 on the next row
	    dc.position += columns - ( dc.position % columns );

	    // if this text overruns the video memory cache, scroll the video up
	    // by the number of lines needed.
	    if( dc.position >= dc.activePage * pageSize + pageSize ) {
	    		scroll( dc, 1 );
	    }
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode#writeCharacter(org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext, int, int, boolean)
	 */
	public int writeCharacter( final IbmPcDisplayContext dc, 
			   final int displayPage, 
			   final int character, 
			   final boolean moveCursor ) {
		// get offset in video memory
		final int offset = ( displayPage * pageSize ) + dc.position;
		
		// offset cannot be outside of video memory
		if( offset >= memorySize ) {
			logger.error(format("Offset (%04X) is greater than memory size (%04X)", offset, memorySize));
		}
		
		// write the character
		dc.memory.setByte( memorySegment, offset, character );
		
		// return the new cursor position
		return moveCursor ? dc.position + 1 : dc.position;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode#writeCharacter(org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext, int, int, int, boolean)
	 */
	public int writeCharacter( final IbmPcDisplayContext dc, 
							   final int displayPage, 
							   final int character, 
							   final int attribute, 
							   final boolean moveCursor ) {
		return writeCharacter( dc, displayPage, character, moveCursor );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#writeText(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, java.lang.String)
	 */
	public int writeText( final IbmPcDisplayContext dc, final int position, final String text ) { 		
		// calculate the position in pixels as (x,y)
		int x = ( position % columns ) * fontWidth;
		int y = ( position / columns ) * fontHeight;
		
		// draw the glyphs
		final char[] chars = text.toCharArray();
		for( char ch : chars ) {
			drawCharacter( dc, x, y, ch );
			x += fontWidth;
		}
		return position + text.length();
	}
	
	/////////////////////////////////////////////////////
	//      Internal Service Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Draws an angled line from (x1,y1) to (x2,y2)
	 * @param x1 the x-coordinate of start point of the line
	 * @param y1 the y-coordinate of start point of the line
	 * @param x2 the x-coordinate of end point of the line
	 * @param y2 the y-coordinate of end point of the line 
	 * @param colorIndex the given color index
	 */
	private void drawAngledLine( IbmPcDisplayContext dc, int x1, int y1, int x2, int y2, int colorIndex ) {
		// determine the X and Y deltas
		final double deltaX	= x2 - x1;
		final double deltaY	= y2 - y1;
		
		// determine horizontal and vertical spans
		final double spanX	= Math.abs( deltaX );
		final double spanY	= Math.abs( deltaY );
		
		// determine start and end values
		final double start;
		final double end;
		if( x2 > x1 ) {
			start	= x1;
			end 	= x2;
		}
		else {
			start	= x2;
			end 	= x1;
		}

		// compute the slope of the line
		final double slope = deltaY / deltaX;
		
		// compute the Y-intercept
		final double yIntercept = y1 - ( slope * x1 );

		// compute the iteration step
		final double step = ( spanX > spanY ) ? 1.0d : ( spanX / spanY ); 		
		
		// draw the line
		for( double x = end; x >= start; x -= step ) {
			final double y = slope * x + yIntercept;				
			drawPixel( dc, (int)x, (int)y, colorIndex );
		}
	}
	
	/**
	 * Draws a horizontal line from (x1,y1) to (x2,y2)
	 * @param x1 the x-coordinate of start point of the line
	 * @param y1 the y-coordinate of start point of the line
	 * @param x2 the x-coordinate of end point of the line
	 * @param y2 the y-coordinate of end point of the line 
	 * @param colorIndex the given color index
	 */
	private void drawHorizontalLine( IbmPcDisplayContext dc, int x1, int y1, int x2, int y2, int colorIndex ) {
		// determine start and end values
		final int start = ( x2 > x1 ) ? x1 : x2;
		final int end = ( x2 > x1 ) ? x2 : x1;

		// create the line
		for( int x = start; x <= end; x++ ) {
			drawPixel( dc, x, y1, colorIndex );
		}
	}
	
	/**
	 * Draws a vertical line from (x1,y1) to (x2,y2)
	 * @param x1 the x-coordinate of start point of the line
	 * @param y1 the y-coordinate of start point of the line
	 * @param x2 the x-coordinate of end point of the line
	 * @param y2 the y-coordinate of end point of the line 
	 * @param colorIndex the given color index
	 */
	private void drawVerticalLine( IbmPcDisplayContext dc, int x1, int y1, int x2, int y2, int colorIndex ) {
		// determine start and end values
		final int start = ( y2 > y1 ) ? y1 : y2;
		final int end = ( y2 > y1 ) ? y2 : y1;

		// create the line
		for( int y = start; y <= end; y++ ) {
			drawPixel( dc, x1, y, colorIndex );
		}
	}
	

}
