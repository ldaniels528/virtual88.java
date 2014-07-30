package ibmpc.devices.display.modes.vga;

import ibmpc.devices.display.IbmPcColors;
import ibmpc.devices.display.IbmPcDisplayContext;
import ibmpc.devices.display.modes.AbstractPackedPixelGraphicsMode;

import java.awt.Color;

/**
 * Represents the VGA 640x480 2-Color (Packed Pixel) Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public class VGAGraphicsMode640x480Mono extends AbstractPackedPixelGraphicsMode {
	private static final Color[] COLOR_MAP		= new Color[] { 
		IbmPcColors.COLORS_16[0], IbmPcColors.COLORS_16[15]
	};
	private static final int COLUMNS		= 80;
	private static final int ROWS			= 25;
	private static final int RES_WIDTH 		= 640;
	private static final int RES_HEIGHT		= 480;
	private static final int COLORS			= 2;
	private static final int MEMORY_SEGMENT	= 0xA000;
	private static final int MEMORY_SIZE	= 262144;
	private static final int PIXELS_PER_BYTE= 8;
	private final int[] pixeldata;

	/**
	 * Default constructor
	 */
	public VGAGraphicsMode640x480Mono() {
		super( 0x11, MEMORY_SEGMENT, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, 8, 16, PIXELS_PER_BYTE, MEMORY_SIZE, COLORS, COLOR_MAP );
		this.pixeldata = new int[PIXELS_PER_BYTE];				
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#readPixel(ibmpc.display.IbmPcDisplayContext, int, int)
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
	 * @see ibmpc.display.modes.AbstractDisplayMode#writePixel(ibmpc.display.IbmPcDisplayContext, int, int, int)
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