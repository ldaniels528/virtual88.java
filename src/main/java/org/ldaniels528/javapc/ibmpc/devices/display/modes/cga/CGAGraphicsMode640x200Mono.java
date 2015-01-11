package org.ldaniels528.javapc.ibmpc.devices.display.modes.cga;

import java.awt.Color;

import static org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColors.COLORS_16;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.AbstractPackedPixelInterleavedGraphicsMode;

/**
 * Represents the CGA 640x200 2-Color (Interleaved/Packed Pixels) Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public class CGAGraphicsMode640x200Mono extends AbstractPackedPixelInterleavedGraphicsMode {
	private static final int EVEN_OFFSET		= 0x0000;
	private static final int ODD_OFFSET			= 0x2000;
	private static final int RES_WIDTH   		= 640;
	private static final int RES_HEIGHT  		= 200;
	private static final int COLUMNS  			= 80;
	private static final int ROWS	  			= 25;
	private static final int COLORS  			= 2;
	private static final int PIXELS_PER_BYTE	= 8;
	private static final int MEMORY_SIZE		= 16384;	
	private static final Color[] COLOR_MAP		= new Color[] { 
		COLORS_16[0], COLORS_16[15]
	};
	private final int[] pixeldata;
	
	/**
	 * Default Constructor
	 */
	public CGAGraphicsMode640x200Mono() {
		super( 0x06, EVEN_OFFSET, ODD_OFFSET, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, 8, 8, 
				PIXELS_PER_BYTE, MEMORY_SIZE, COLORS, COLOR_MAP );
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
		final int multiplier = 7 - pixelIndex;
		final int colorIndex = ( pixeldata >> multiplier ) & 0x01;
		
		// return the color
		return colorIndex;
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.AbstractDisplayMode#writePixel(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public void drawPixel( IbmPcDisplayContext dc, int x, int y, int colorIndex ) {
		// get the segment portion of the address
		final int offset	 = getPixelOffset( x, y );
		
		// get the pixel data at the given segment and offset
		final int pixelIndex = ( x % PIXELS_PER_BYTE );
		final int multiplier = 7 - pixelIndex;
		
		// write the pixel to memory
		dc.memory.setBits( memorySegment, offset, ( colorIndex & 0x01 ) << multiplier );
	}
	
	/**
	 * Decodes the packed pixels and draws them to the screen
	 * <pre>
	 * Bits	Description
	 * ----	-----------
	 * 0		Eighth pixel in byte
     * 1		Seventh pixel in byte
     * 2		Sixth pixel in byte
     * 3		Fifth pixel in byte
     * 4		Fourth pixel in byte
     * 5		Third pixel in byte
     * 6		Second pixel in byte
     * 7		First pixel in byte
     * </pre>
	 * @param packedPixels the given packed pixels 
	 */
	protected int[] decodePackedPixels( int packedPixels ) {
		pixeldata[0] = ( packedPixels & 0x80 ) >> 7;	// 10000000
		pixeldata[1] = ( packedPixels & 0x40 ) >> 6;	// 01000000
		pixeldata[2] = ( packedPixels & 0x20 ) >> 5;	// 00100000
		pixeldata[3] = ( packedPixels & 0x10 ) >> 4;	// 00010000
		pixeldata[4] = ( packedPixels & 0x08 ) >> 3;	// 00001000
		pixeldata[5] = ( packedPixels & 0x04 ) >> 2;	// 00000100
		pixeldata[6] = ( packedPixels & 0x02 ) >> 1;	// 00000010
		pixeldata[7] = ( packedPixels & 0x01 );			// 00000001
		return pixeldata;
	}

}
