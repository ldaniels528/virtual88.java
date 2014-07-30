package ibmpc.devices.display.modes;

import static ibmpc.devices.display.fonts.IbmPcFont8x8.FONTS_8X8;
import static ibmpc.devices.display.fonts.IbmPcFont8x8.PIXEL_ON_MASK;
import static ibmpc.devices.display.fonts.IbmPcFont8x8.PIXEL_ZERO_MASK;
import ibmpc.devices.display.IbmPcDisplayContext;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a Packed Pixel Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractPackedPixelGraphicsMode extends AbstractGraphicsMode {
	protected final Color[] colorMap;	
	protected final int pixelsPerByte;
	protected final int blockSize;
	protected final byte[] block;
	
	/////////////////////////////////////////////////////
	//      Constructor(s)
	/////////////////////////////////////////////////////

	/**
	 * Default constructor
	 */
	public AbstractPackedPixelGraphicsMode( int videoMode, 
								  		  int memorySegment,
								  		  int columns, int rows, 
								  		  int width, int height, 
								  		  int fontWidth, int fontHeight,
								  		  int pixelsPerByte, 
								  		  int memorySize, 
								  		  int colors, 
								  		  Color[] colorMap ) {
		super( videoMode, memorySegment, columns, rows, width, height, fontWidth, fontHeight, colors, pixelsPerByte, memorySize );
		this.pixelsPerByte 	= pixelsPerByte;
		this.colorMap		= colorMap;
		this.blockSize		= width / pixelsPerByte;
		this.block			= new byte[blockSize];
	}
	
	/////////////////////////////////////////////////////
	//      Service Method(s)
	/////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#scroll(ibmpc.display.IbmPcDisplayContext, int)
	 */
	public void scroll( IbmPcDisplayContext dc, int nRows ) {
		// TODO Auto-generated method stub		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#writeCharacter(ibmpc.display.IbmPcDisplayContext, int, int, char)
	 */
	public void drawCharacter( IbmPcDisplayContext dc, int x, int y, char ch ) {
		// determine the first byte of the font data
		final int charIndex = ch << 3;
		
		// iterate each byte for this glyph		
		for( int n = 0; n < 8; n++ ) {
			final byte bytedata = FONTS_8X8[ charIndex | n ];
			if( bytedata == 0 ) continue; 
			
			// draw the pixels	
			for( int m = 0; m < 8; m++ ) {				
				// draw a single pixel
				if( ( bytedata & PIXEL_ON_MASK[m] ) > 0 ) {
					drawPixel( dc, x + m, y + n, dc.color.getForeground() );
				}
				// otherwise, if the rest of the bits are zero, break out
				else if( ( bytedata & PIXEL_ZERO_MASK[m] ) == 0 ) break;
			}		
		}				
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#render(ibmpc.display.IbmPcDisplayContext, java.awt.Graphics2D)
	 */
	public void render( IbmPcDisplayContext dc, Graphics2D g ) {
		// get the x and y-scale factors
		final double xscale = dc.frame.getPaneWidth() / width;
		final double yscale = dc.frame.getPaneHeight() / height;	
		
		// blit the graphics to the display
		int offset = 0;
		for( int scanLine = 0; scanLine < height; scanLine++ ) {			
			// read a block of video memory
			dc.memory.getBytes( memorySegment, offset, block, block.length );
			
			// calculate the physical Y-coordinate for this scan line
			final int y = (int)( yscale * scanLine );
			
			// interpret the block
			renderPixels( g, block, xscale, y );
			
			// advance the offset
			offset += blockSize;
		}
	}
	
	/////////////////////////////////////////////////////
	//      Internal Service Method(s)
	/////////////////////////////////////////////////////

	/**
	 * Decodes the packed pixels and draws them to the screen
	 * @param packedPixels the given packed pixels 
	 */
	protected abstract int[] decodePackedPixels( int packedPixels );

	/**
	 * Returns the memory offset for the pixel residing at the given (x,y) coordinate
	 * @param x the given x-coordinate
	 * @param y the given y-coordinate
	 * @return the memory offset for the pixel residing at the given (x,y) coordinate
	 */
	protected int getPixelOffset( int x, int y ) {
		return ( y * blockSize ) + ( x / pixelsPerByte );
	}
	
	/**
	 * Decodes the packed pixels and draws them to the screen
	 * @param g the given {@link Graphics2D graphics context}
	 * @param block the given binary video block 
	 */
	protected void renderPixels( Graphics2D g, byte[] block, double xscale, int y ) {		 
		for( int n = 0; n < block.length; n++ ) {
			// decode the packed pixels
			final int[] pixels = decodePackedPixels( block[n] );
			
			// calculate the x-position of the first pixel
			final int xp = n * pixels.length; 
			
			// draw the pixels
			int index = 0;
			while( index < pixels.length ) {
				// get the current color
				final int color = pixels[index];
				
				// count the number of pixels that are the same color for the same span
				int span = 1;
				while( ( index + span < pixels.length ) && 
						( pixels[ index + span ] == color ) ) span++;
				
				// calculate first x-position + index
				final int xpi = xp + index;
				
				// single pixel?
				if( span == 1 ) {
					final int x = (int)( xscale * xpi );
					renderSinglePixel( g, x, y, color );
				}
				
				// multiple pixels ..
				else {
					final int x1 = (int)( xscale * xpi );
					final int x2 = (int)( xscale * ( xpi + span ) );
					renderMultiplePixels( g, x1, y, x2, y, color );
				}
					
				index += span;
			}
		}
	}

	/**
	 * Draws a single pixel to the screen at position (x,y) having the given color
	 * @param g the given {@link Graphics2D graphics context}
	 * @param x the given x-coordinate
	 * @param y the given y-coordinate
	 * @param color the given color index
	 */
	protected void renderSinglePixel( Graphics2D g, int x, int y, int color ) {
		// set the pixel's color
		g.setColor( colorMap[color] );
		
		// draw the pixel
		for( int m = -1; m <= 1; m++ ) {
			for( int n = -1; n <= 1; n++ ) {
				final int xm = x + m;
				final int yn = y + n;
				g.drawLine( xm, yn, xm, yn );
			}
		}
	}
	
	/**
	 * Draws a single pixel to the screen at position (x,y) having the given color
	 * @param g the given {@link Graphics2D graphics context}
	 * @param x1 the given x-coordinate
	 * @param y1 the given y-coordinate
	 * @param color the given color index
	 */
	protected void renderMultiplePixels( Graphics2D g, int x1, int y1, int x2, int y2, int color ) {
		// set the pixel's color
		g.setColor( colorMap[color] );
		
		// draw the pixel
		for( int n = -1; n <= 1; n++ ) {
			g.drawLine( x1, y1 + n, x2, y2 + n );
		}
	}

}
