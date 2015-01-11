/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.devices.display.modes.ega;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColors;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.AbstractPackedPixelGraphicsMode;

import java.awt.Color;

/**
 * Represents the EGA 640x200 16-Color (Packed Pixel) Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public class EGAGraphicsMode640x200 extends AbstractPackedPixelGraphicsMode {
	private static final Color[] COLOR_MAP		= IbmPcColors.COLORS_16;
	private static final int COLUMNS			= 80;
	private static final int ROWS				= 25;
	private static final int RES_WIDTH 		= 640;
	private static final int RES_HEIGHT		= 200;
	private static final int COLORS			= 16;
	private static final int MEMORY_SEGMENT	= 0xA000;
	private static final int MEMORY_SIZE		= 65536;
	private static final int PIXELS_PER_BYTE	= 2;
	private final int[] pixeldata;	

	/**
	 * Default constructor
	 */
	public EGAGraphicsMode640x200() {
		super( 0x0E, MEMORY_SEGMENT, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, 8, 14, PIXELS_PER_BYTE, MEMORY_SIZE, COLORS, COLOR_MAP );
		this.pixeldata = new int[PIXELS_PER_BYTE];		
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.AbstractDisplayMode#readPixel(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public int readPixel( IbmPcDisplayContext dc, int x, int y ) {
		// get the segment portion of the address
		final int offset	 = getPixelOffset( x, y );
		
		// get the pixel data at the given segment and offset
		final int pixeldata = dc.memory.getByte( memorySegment, offset );
		
		// isolate the single pixel's color
		final int pixelIndex = ( x % PIXELS_PER_BYTE );
		final int multiplier = pixelIndex << 2;
		final int colorIndex = ( pixeldata >> multiplier ) & 0xF;
		
		// return the color
		return colorIndex;
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.AbstractDisplayMode#writePixel(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int, int)
	 */
	public void drawPixel( IbmPcDisplayContext dc, int x, int y, int colorIndex ) {
		// get the segment portion of the address
		final int offset	 = getPixelOffset( x, y );
		
		// get the pixel data at the given segment and offset
		final int pixelIndex = ( x % PIXELS_PER_BYTE ); 
		final int multiplier = pixelIndex << 2;
		
		// write the pixel to memory
		dc.memory.setBits( memorySegment, offset, ( colorIndex & 0x0F ) << multiplier );		
	}
	
	/**
	 * Decodes the packed pixels and draws them to the screen
	 * <pre>
	 * Bits	Description
	 * ----	-----------
     * 0-3	second pixel in byte
     * 4-7	first pixel in byte
     * </pre>
	 * @param packedPixels the given packed pixels 
	 */
	protected int[] decodePackedPixels( int packedPixels ) {
		pixeldata[0] = ( packedPixels & 0xF0 ) >> 4;	// 11110000
		pixeldata[1] = ( packedPixels & 0x0F );		// 00001111
		return pixeldata;
	}
	
}
