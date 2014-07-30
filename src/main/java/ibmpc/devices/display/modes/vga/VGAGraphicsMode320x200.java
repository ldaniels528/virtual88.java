package ibmpc.devices.display.modes.vga;

import static ibmpc.devices.display.IbmPcColors.COLORS_256;
import ibmpc.devices.display.IbmPcDisplayContext;
import ibmpc.devices.display.modes.AbstractPackedPixelGraphicsMode;

/**
 * Represents the VGA 320x200 256-Color (Linear/Packed Pixels) Graphics Mode
 * @author lawrence.daniels@gmail.com
 */
public class VGAGraphicsMode320x200 extends AbstractPackedPixelGraphicsMode {	
	private static final int COLUMNS			= 40;
	private static final int ROWS				= 25;
	private static final int RES_WIDTH 			= 320;
	private static final int RES_HEIGHT			= 200;
	private static final int COLORS				= 256;
	private static final int MEMORY_SEGMENT		= 0xA000;
	private static final int MEMORY_SIZE		= 262144;
	private static final int PIXELS_PER_BYTE	= 1;
	private final int[] pixeldata;	

	/**
	 * Default constructor
	 */
	public VGAGraphicsMode320x200() {
		super( 0x13, MEMORY_SEGMENT, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, 8, 16, 
				PIXELS_PER_BYTE, MEMORY_SIZE, COLORS, COLORS_256 );
		this.pixeldata = new int[PIXELS_PER_BYTE];
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#readPixel(ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public int readPixel( IbmPcDisplayContext dc, int x, int y ) {
		// get the segment portion of the address
		final int offset	 = getPixelOffset( x, y );
		
		// get the pixel data at the given segment and offset
		final int colorIndex = dc.memory.getByte( memorySegment, offset );
		
		// return the color
		return colorIndex;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#writePixel(ibmpc.display.IbmPcDisplayContext, int, int, int)
	 */
	public void drawPixel( IbmPcDisplayContext dc, int x, int y, int colorIndex ) {
		// get the segment portion of the address
		final int offset	= getPixelOffset( x, y );
		
		// write the pixel to memory
		dc.memory.setBits( memorySegment, offset, (byte)colorIndex );		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.PackedPixelGraphicsMode#getPixelOffset(int, int)
	 */
	protected int getPixelOffset( int x, int y ) {
		return y * RES_WIDTH + x;		
	}
	
	/**
	 * Decodes the packed pixels and draws them to the screen
	 * <pre>
	 * Bits	Description
	 * ----	-----------
     * 0-7	first pixel in byte
     * </pre>
	 * @param packedPixels the given packed pixels 
	 */
	protected int[] decodePackedPixels( int packedPixels ) {
		pixeldata[0] = packedPixels;		
		return pixeldata;
	}
	
}
