package org.ldaniels528.javapc.ibmpc.devices.display.modes.cga;

import static org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColors.COLORS_16;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.AbstractPackedPixelInterleavedGraphicsMode;

import java.awt.Color;

/**
 * Represents the CGA 320x200 4-Color (Interleaved/Packed Pixels) Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public class CGAGraphicsMode320x200 extends AbstractPackedPixelInterleavedGraphicsMode {
	private static final int EVEN_OFFSET		= 0x0000;
	private static final int ODD_OFFSET			= 0x2000;
	private static final int RES_WIDTH   		= 320;
	private static final int RES_HEIGHT  		= 200;
	private static final int COLUMNS  			= 40;
	private static final int ROWS	  			= 25;
	private static final int COLORS  			= 4;
	private static final int PIXELS_PER_BYTE	= 4;
	private static final int MEMORY_SIZE		= 16384;
	private static final Color[][] COLOR_MAP	= new Color[][] {
		new Color[] { COLORS_16[0], COLORS_16[3], COLORS_16[5], COLORS_16[15] },
		new Color[] { COLORS_16[0], COLORS_16[2], COLORS_16[4], COLORS_16[6] }
	};
	private final int[] pixeldata;

	/**
	 * Default constructor
	 */
	public CGAGraphicsMode320x200() {
		super( 0x04, EVEN_OFFSET, ODD_OFFSET, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, 8, 8, PIXELS_PER_BYTE, MEMORY_SIZE, COLORS, COLOR_MAP[0] );
		this.pixeldata = new int[PIXELS_PER_BYTE];
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#readPixel(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public int readPixel( IbmPcDisplayContext dc, int x, int y ) {
		// get the segment portion of the address
		final int offset	 = getPixelOffset( x, y );
		
		// get the pixel data at the given segment and offset
		final int pixeldata = dc.memory.getByte( memorySegment, offset );
		
		// isolate the single pixel's color
		final int pixelIndex = ( x % PIXELS_PER_BYTE );
		final int multiplier = 6 - ( pixelIndex << 1 );
		final int colorIndex = ( pixeldata >> multiplier ) & 0x03;
		
		// return the color
		return colorIndex;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#writePixel(org.ldaniels528.javapc.ibmpc.display.IbmPcDisplayContext, int, int, int)
	 */
	public void drawPixel( IbmPcDisplayContext dc, int x, int y, int colorIndex ) {
		// get the segment portion of the address
		final int offset	 = getPixelOffset( x, y );
		
		// get the pixel data at the given segment and offset
		final int pixelIndex = ( x % PIXELS_PER_BYTE ); // 0..3
		final int multiplier = 6 - ( pixelIndex << 1 ); // 6 - ( pixelIndex * 2 )
		
		// write the pixel to memory
		dc.memory.setBits( memorySegment, offset, ( colorIndex & 0x03 ) << multiplier );		
	}

	/**
	 * Decodes the packed pixels and draws them to the screen
	 * <pre>
	 * Bits	Description
	 * ----	-----------
     * 0-1	fourth pixel in byte
     * 2-3	third pixel in byte
     * 4-5	second pixel in byte
     * 6-7	first pixel in byte
     * </pre>
	 * @param packedPixels the given packed pixels 
	 */
	protected int[] decodePackedPixels( int packedPixels ) {
		pixeldata[0] = ( packedPixels & 0xC0 ) >> 6;	// 11000000
		pixeldata[1] = ( packedPixels & 0x30 ) >> 4;	// 00110000
		pixeldata[2] = ( packedPixels & 0x0C ) >> 2;	// 00001100
		pixeldata[3] = ( packedPixels & 0x03 );			// 00000011
		return pixeldata;
	}

}
