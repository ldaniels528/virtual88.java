package org.ldaniels528.javapc.ibmpc.devices.display.modes;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a Packed Pixel/Interleaved Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractPackedPixelInterleavedGraphicsMode extends AbstractPackedPixelGraphicsMode {
	private static final int MEMORY_SEGMENT	= 0xB800;
	private final int evenOffset;
	private final int oddOffset;

	/////////////////////////////////////////////////////
	//      Constructor(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Default constructor
	 */
	public AbstractPackedPixelInterleavedGraphicsMode( int videoMode, 
									   		 		int evenOffset, int oddOffset, 
									   		 		int columns, int rows, 
									   		 		int width, int height, 
									   		 		int fontWidth, 
									   		 		int fontHeight, 
									   		 		int pixelsPerByte, 
									   		 		int memorySize, 
									   		 		int colors, 
									   		 		Color[] colorMap ) {		
		super( videoMode, MEMORY_SEGMENT, 
				columns, rows, width, height, 
				fontWidth, fontHeight, pixelsPerByte, 
				memorySize, colors, colorMap );
		this.evenOffset = evenOffset;
		this.oddOffset  = oddOffset;
	}
	
	/////////////////////////////////////////////////////
	//      Service Method(s)
	/////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.AbstractDisplayMode#render(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, java.awt.Graphics2D)
	 */
	@Override
	public void render( final IbmPcDisplayContext dc, final Graphics2D g ) {
		// get the x and y-scale factors
		final double xscale = dc.frame.getPaneWidth() / width;
		final double yscale = dc.frame.getPaneHeight() / height;	
		
		// blit the graphics to the display		
		for( int scanLine = 0; scanLine < height; scanLine++ ) {
			// retrieves the offset of this scan line
			final int offset = getBlockOffset( scanLine );
			
			// read a block of video memory
			dc.memory.getBytes( memorySegment, offset, block, block.length );
			
			// calculate the physical Y-coordinate for this scan line
			final int y = (int)( yscale * scanLine );
			
			// interpret the block
			renderPixels( g, block, xscale, y );
		}
	}
	
	/////////////////////////////////////////////////////
	//      Internal Service Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Returns the memory offset for the block containing the given scan line
	 * @param scanLine the given scan line
	 * @return the memory offset for the block containing the given scan line
	 */
	private int getBlockOffset( final int scanLine ) {
		return ( ( scanLine % 2 == 1 ) ? oddOffset : evenOffset )  + ( blockSize * ( scanLine >> 1 ) );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.PackedPixelGraphicsMode#getPixelOffset(int, int)
	 */
	protected int getPixelOffset( final int x, final int y ) {
		return getBlockOffset( y ) + ( x / pixelsPerByte );
	}

}
