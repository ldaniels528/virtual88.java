package ibmpc.devices.display.modes;

import static ibmpc.devices.display.fonts.IbmPcFont8x8.FONTS_8X8;
import static ibmpc.devices.display.fonts.IbmPcFont8x8.PIXEL_ON_MASK;
import static ibmpc.devices.display.fonts.IbmPcFont8x8.PIXEL_ZERO_MASK;
import ibmpc.devices.display.IbmPcColors;
import ibmpc.devices.display.IbmPcDisplayContext;
import ibmpc.devices.display.IbmPcDisplayException;
import ibmpc.util.Logger;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a Generic Text Mode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractTextMode extends AbstractDisplayMode {
	private static final Color[] COLOR_MAP = IbmPcColors.COLORS_16;
	private static final byte SPACE = 0x20;
	private static final int CHAR_WIDTH = 2;	
	private final int physicalColumns;
	private final byte[] bytedata;
	private final byte[] chardata;

	/**
	 * Default constructor
	 */
	public AbstractTextMode( final int videoMode, 
					   	  	 final int memorySegment, 
					   	  	 final int columns, 
					   	  	 final int rows, 
					   	  	 final int width, 
					   	  	 final int height, 
					   	  	 final int fontWidth, 
					   	  	 final int fontHeight, 
					   	  	 final int colors ) {
		super( videoMode, 
			   memorySegment, 
			   columns, rows, 
			   width, height, 
			   fontWidth, fontHeight, 
			   colors, 
			   columns * rows * CHAR_WIDTH, 
			   16384, 
			   false );
		this.physicalColumns= columns << 1;
		this.bytedata 		= new byte[physicalColumns];
		this.chardata 		= new byte[columns];
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#backspace(ibmpc.display.IbmPcDisplayContext)
	 */
	public void backspace( final IbmPcDisplayContext dc ) {
		if( dc.position > 0 ) {
			final int attrChar = ( dc.color.asAttribute() << 4 ) | 0x20; 
			dc.position -= CHAR_WIDTH;			
			dc.memory.setWord( memorySegment, dc.position, attrChar );
		}	
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#clear(ibmpc.display.IbmPcDisplayContext)
	 */
	public void clear( final IbmPcDisplayContext dc ) {
		// create a word representing the a space character
		// and the color attribute
		final byte attribute = dc.color.asAttribute();
	
		// prepare a block for blitting
		final byte[] tempdata = new byte[physicalColumns];
		fillCharacterAttributeBlock( tempdata, 0, tempdata.length, attribute );
	
		// clear the screen (in video memory)
		for( int row = 0; row < rows; row++ ) {
			dc.memory.setBytes( memorySegment, row * tempdata.length, tempdata, tempdata.length );
		}
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#drawLine(ibmpc.display.IbmPcDisplayContext, int, int, int, int)
	 */
	public void drawLine( final IbmPcDisplayContext dc, int x1, int y1, int x2, int y2, int colorIndex) {
		throw new IbmPcDisplayException( "Illegal display mode" );		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#getCursorPosition(ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public int getCursorPosition( final IbmPcDisplayContext dc, int column, int row ) {
		final int pageOffset = dc.activePage * pageSize;
		return pageOffset + ( ( row - 1 ) * physicalColumns ) + ( ( column - 1 ) << 1 );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#newLine(ibmpc.display.IbmPcDisplayContext)
	 */
	public void newLine( final IbmPcDisplayContext dc ) {
	    // goto column 1 on the next row
	    dc.position += physicalColumns - ( dc.position % physicalColumns );

	    // compute the page offset in memory
	    final int pageStart = dc.activePage * pageSize;
	    
	    // compute the memory offset limit
	    final int limit = pageStart + pageSize - ( getCaptionRows() * physicalColumns );
	    
	    // determine how much to scroll
	    final int scrollAmount = 1;
	    
	    // if this text overruns the video memory cache, scroll the video up
	    // by the number of lines needed.
	    if( dc.position >= limit ) {
	    	scroll( dc, scrollAmount );
	    }
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.IbmPcDisplayMode#readPixel(ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public int readPixel( final IbmPcDisplayContext dc, final int x, final int y ) {
		throw new IbmPcDisplayException( "Illegal display mode" );		
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.modes.IbmPcDisplayMode#scroll(ibmpc.display.IbmPcDisplayContext, int)
	 */
	public void scroll( final IbmPcDisplayContext dc, final int numberOfRows ) {		
	    // create a temp buffer (filled with zeroes)
	    final byte[] tempdata = new byte[pageSize];	

	    // calculate the offset in memory * rows from the top
	    final int offset = numberOfRows * physicalColumns; 
	    
	    // calculate the length of bytes to be copied
	    final int length = pageSize - offset;

	    // fill the remaining area with a space and the current attribute
	    fillCharacterAttributeBlock( tempdata, offset, tempdata.length - offset, dc.color.asAttribute() );
	    
	    // copy the portion of memory to the temp buffer
	    dc.memory.getBytes( memorySegment, offset, tempdata, length );
	    
	    // copy the temp buffer to the video memory cache    
	    dc.memory.setBytes( memorySegment, 0, tempdata, tempdata.length );		
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.IbmPcDisplayMode#render(ibmpc.display.IbmPcDisplayContext, java.awt.Graphics2D)
	 */
	public void render( final IbmPcDisplayContext dc, final Graphics2D g ) {
		// get the x and y-scale factors
		final double xscale = dc.frame.getPaneWidth() / width;
		final double yscale = dc.frame.getPaneHeight() / height;;
		
		// calculate the page offset
		final int pageOffset = dc.activePage * pageSize;
		
		// blit each row of data to video memory
		int offset = pageOffset;
		for( int row = 0; row < rows; row++ ) {
			// copy 1 column's worth of values to our buffer
			dc.memory.getBytes( memorySegment, offset, bytedata, bytedata.length );

			// convert the byte values to a character string
			expandToAscii( bytedata, chardata );

			// draw the text
			int index = 0;
			while( index < chardata.length ) {
				// decode color attributes (foreground and background)
				final byte attribute = bytedata[(index << 1) + 1];

				// how many characters use the same color attributes [on this row]?
				int span = index;
				while( span < chardata.length
						&& bytedata[(span << 1) + 1] == attribute ) span++;

				// render the characters onto the display
				renderText( g, chardata, index, span, attribute, row, xscale, yscale );
				
				// update the index position
				index = span;
			}
			
			// advance the offset position
			offset += physicalColumns;
		}
		
		// if the caption is set ...
		if( hasCaption() ) {
			// the caption shall appear on the last row
			final int row = ( rows - getCaptionRows() );
			
			// render the caption text onto the display
			renderText( g, caption.getBytes(), 0, caption.length(), dc.color.asAttribute(), row, xscale, yscale );
		}
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#drawCharacter(ibmpc.display.IbmPcDisplayContext, int, int, char)
	 */
	public void drawCharacter( final IbmPcDisplayContext dc, int x, int y, char ch ) {
		throw new IbmPcDisplayException( "Illegal display mode" );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#drawPixel(ibmpc.display.IbmPcDisplayContext, int, int)
	 */
	public void drawPixel( final IbmPcDisplayContext dc, int x, int y, int colorIndex ) {
		throw new IbmPcDisplayException( "Illegal display mode" );		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.display.modes.IbmPcDisplayMode#writeCharacter(ibmpc.devices.display.IbmPcDisplayContext, int, int, boolean)
	 */
	public int writeCharacter( final IbmPcDisplayContext dc, 
							   final int displayPage, 
							   final int character, 
							   final boolean moveCursor ) {
		// get offset in video memory
		final int offset = ( displayPage * pageSize ) + dc.position;
		
		// offset cannot be outside of video memory
		if( offset >= memorySize ) {
			Logger.error( "Offset (%04X) is greater than memory size (%04X)\n", offset, memorySize );
		}
		
		// get the current attribute
		final int attribute = dc.memory.getByte( memorySegment, offset ) & 0x0F;
		
		// create the character attribute
		final int chrattr = ( character << 8 ) | attribute;
		
		// write the character attribute
		dc.memory.setByte( memorySegment, offset, chrattr );
		
		// return the new cursor position
		return moveCursor ? dc.position + 1 : dc.position;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.display.modes.IbmPcDisplayMode#writeCharacter(ibmpc.devices.display.IbmPcDisplayContext, int, int, int, boolean)
	 */
	public int writeCharacter( final IbmPcDisplayContext dc, 
							   final int displayPage, 
							   final int character, 
							   final int attribute, 
							   final boolean moveCursor ) {
		// get offset in video memory
		final int offset = ( displayPage * pageSize ) + dc.position;
		
		// offset cannot be outside of video memory
		if( offset >= memorySize ) {
			Logger.error( "Offset (%04X) is greater than memory size (%04X)\n", offset, memorySize );
		}
		
		// create the character attribute
		final int chrattr = ( character << 8 ) | attribute;
		
		// write the character attribute
		dc.memory.setByte( memorySegment, offset, chrattr );
		
		// return the new cursor position
		return moveCursor ? dc.position + 1 : dc.position;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.modes.AbstractDisplayMode#writeText(ibmpc.display.IbmPcDisplayContext, int, java.lang.String)
	 */
	public int writeText( final IbmPcDisplayContext dc, int position, final String text ) {		
		// get the length of the text
		final int length = text.length();

		// compute the mode dependent column length
		final int physicalLength = length << 1;
		
		// calculate the extents of the screen
		final int limit = physicalColumns * ( rows - getCaptionRows() );
		 	  
		// if this text overruns video memory, scroll the video up
		// by the number of lines needed.
		if( position + physicalLength > limit ) {
			// calculate the number of lines needed for this text
			final int linesRequired = length / columns;		
	    		
			// scroll video memory
			scroll( dc, linesRequired );
	    		
			// update the cursor position
			position -= physicalColumns;
		}
	  
		// convert the text string to binary
		final byte[] bytedata = encodeCharacters( dc, text );
		  
		// write the text to video memory
		dc.memory.setBytes( memorySegment, position, bytedata, bytedata.length );
		  
		// increase the positional offset
		position += physicalLength;
		
		// return the new cursor position
		return position;
	}

	/**
	 * Draws the characters to the display directly
	 * @param g the given {@link Graphics2D graphics context}
	 * @param chardata the given character data string
	 * @param offset the given offset
	 * @param length the given length
	 * @param x the given x-coordinate position
	 * @param y the given y-coordinate position
	 * @param xscale the given x-axis scale
	 * @param yscale the given y-axis scale
	 */
	private void drawChars( final Graphics2D g, 
						    final byte[] chardata, 
						    final int offset, 
						    final int length, 
						    int x, 
						    final int y, 
						    final double xscale, 
						    final double yscale ) {	
		for( int pos = 0; pos < length; pos++ ) {
			// grab the current character
			final byte b = chardata[offset + pos];
			final int ch = b < 0 ? b + 256 : b;
			
			// determine the first byte of the font data
			final int fontIndex = ch << 3;
			
			// iterate each byte for this glyph		
			for( int n = 0; n < 8; n++ ) {
				final byte packedByte = FONTS_8X8[fontIndex | n];
				if( packedByte != 0 ) { 				
					// draw the pixels	
					for( int m = 0; m < 8; m++ ) {				
						// draw a single pixel
						if( ( packedByte & PIXEL_ON_MASK[m] ) > 0 ) {
							int px = (int)( xscale * (double)( x + m ) );
							int py = (int)( yscale * (double)( y + n ) );
							
							for( int r = -1; r <= 1; r++ ) {
								g.drawLine( px, py + r, px, py + r );
							}
						}
						// otherwise, if the rest of the bits are zero, break out
						else if( ( packedByte & PIXEL_ZERO_MASK[m] ) == 0 ) m = 8;
					}		
				}
			}
			// advance to the next position
			x += 8;			
		}
	}

	/**
	 * Encodes the given text string into an 
	 * array of binary data suitable for writting 
	 * to video memory
	 * @param text the given text string
	 * @return an array of binary data suitable for writting 
	 * to video memory
	 */
	private byte[] encodeCharacters( final IbmPcDisplayContext dc, final String text ) {
		// get the background and foreground colors
		final int bgc = dc.color.getBackground();
		final int fgc = dc.color.getForeground();
	
		// get the source data
		final byte[] srcdata = text.getBytes();
	
		// determine how big the block of data should be
		final int blockSize = srcdata.length << 1;
	
		// allocate the block
		final byte[] bytedata = new byte[blockSize];
	
		// build the binary string
		for (int index = 0; index < srcdata.length; index++) {
			// compute the color attribute for this character
			final byte attribute = (byte) ((bgc << 4) | fgc);
	
			// place the information for each character into the string
			final int index2 = index << 1;
			bytedata[index2] = srcdata[index];
			bytedata[index2 + 1] = attribute;
		}
	
		return bytedata;
	}
	
	/**
	 * Expands the given binary string to a GWBASIC compatible ASCII string
	 * @param bytedata the given binary string
	 * @param chardata the given buffer for the ASCII string
	 */
	private void expandToAscii( final byte[] bytedata, final byte[] chardata ) {
		for( int index = 0; index < chardata.length; index++ ) {				  
			// get the ascii code
			final byte bytcode  = bytedata[index << 1];					  			 
			  
			// append the buffer with the appropriate unicode character
			chardata[index] = bytcode;			  
		}
	}
	
	/**
	 * Fills the given block with the an attribute/space combination
	 * for clearing the screen 
	 * @param block the given block to fill
	 * @param offset the starting offset
	 * @param length the length of the span to populate
	 * @param attribute the given color attribute (background+foreground)
	 */
	private void fillCharacterAttributeBlock( final byte[] block, 
											  final int offset, 
											  final int length, 
											  final byte attribute ) {
		final int end = offset + length;
		for( int index = offset; index < end; index += CHAR_WIDTH ) {
			block[index] = SPACE;
			block[index + 1] = attribute;
		}
	}
	
	/**
	 * Render the background in the given color starting 
	 * at the given index for the given span.
	 * @param g the given {@link Graphics2D graphics context}
	 * @param index the given starting index
	 * @param span the given span
	 * @param ypos the given y-position
	 * @param bgc the given background color
	 */
	private void renderBackground( final Graphics2D g, 
								   final int index,  
								   final int span, 
								   final int ypos, 
								   final int bgc, 
								   final double xscale, 
								   final double yscale ) {
		// calculate the x and y positions
		final int x = (int)( xscale * index * fontWidth );
		final int y = (int)( yscale * ypos ) - 1;
		final int w = (int)( xscale * span * fontWidth );
		final int h = (int)( yscale * fontHeight ) + 1;
		
		// draw the filled rectangle
		g.setColor( IbmPcColors.getColor( bgc ) );
		g.fillRect( x, y, w, h );
	}

	/**
	 * Renders the given characters onto the screen from 
	 * the given index (start position) to the given 
	 * span (end position)
	 * @param g the given {@link Graphics2D graphics context}
	 * @param chardata the given character data
	 * @param index the given starting position (within the text data) of the text to be rendered
	 * @param span the given ending position of the text to be rendered
	 * @param attribute the given packed color attribute
	 * @param row the given row of the rendered text
	 * @param xscale the X-axis scale of the display
	 * @param yscale the Y-axis scale of the display
	 */
	private void renderText( final Graphics2D g, 
							 final byte[] chardata,
							 final int index,
							 final int span,
							 final byte attribute,
							 final int row,
							 final double xscale, 
							 final double yscale ) {
		// compute the length of the text that is to be displayed
		final int count = span - index; 
	
		// calculate the X- and Y-axis positions of the character text
		final int xpos = index * fontWidth;
		final int ypos = row * fontHeight;
	
		// compute the foreground and background colors
		final int fgc = attribute & 0x0F;
		final int bgc = ( attribute & 0xF0 ) >> 4;
		
		// draw the background				
		renderBackground( g, index, span, ypos, bgc, xscale, yscale );
	
		// draw the character(s)
		g.setColor( COLOR_MAP[ fgc ] );
		drawChars( g, chardata, index, count, xpos, ypos, xscale, yscale );
	}
	
}