package ibmpc.devices.display.modes;

import java.awt.Graphics2D;

import ibmpc.devices.display.IbmPcDisplayContext;
import ibmpc.devices.display.IbmPcDisplayException;

/**
 * Represents an IBM PC/XT/AT Display Mode
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcDisplayMode {	
	
	/////////////////////////////////////////////////////
	//      Service Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Performs a backspace operation
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 */
	void backspace( IbmPcDisplayContext dc );
	
	/**
	 * Clears the screen
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 */
	void clear( IbmPcDisplayContext dc );
	
	/**
	 * Copies the source page to the target page
	 * <br><b>NOTE</b>: Page #1 is index 0
	 * @param sourcePage the given source page
	 * @param targetPage the given target page
	 * @throws IbmPcDisplayException
	 */
	void copyPage( IbmPcDisplayContext dc, int sourcePage, int targetPage );
	
	/**
	 * Draws a line from (x1,y1) to (x2,y2)
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param x1 the x-coordinate of start point of the line
	 * @param y1 the y-coordinate of start point of the line
	 * @param x2 the x-coordinate of end point of the line
	 * @param y2 the y-coordinate of end point of the line 
	 * @param colorIndex the given color index
	 */
	void drawLine( IbmPcDisplayContext dc, int x1, int y1, int x2, int y2, int colorIndex );
	
	/**
	 * Returns the position of virtual cursor at the given 
	 * row and column locations.
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param column the given column
	 * @param row the given row
	 * @param page the given display page
	 */
	int getCursorPosition( IbmPcDisplayContext dc, int column, int row );
	
	/**
	 * Forces the virtual cursor to the next line
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 */
	void newLine( IbmPcDisplayContext dc );
	
	/**
	 * Reads the pixel at (x,y)
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param x the given x-coordinate
	 * @param y the given y-coordinate
	 * @return the color of the pixel at (x,y)
	 */
	int readPixel( IbmPcDisplayContext dc, int x, int y );
	
	/**
	 * Scrolls video memory up by the given number of rows
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param nRows the given number of rows
	 */
	void scroll( IbmPcDisplayContext dc, int nRows );
	
	/**
	 * Renders the display based on current video memory
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param g the given {@link Graphics2D graphics context}
	 */
	void render( IbmPcDisplayContext dc, Graphics2D g );
	
	/** 
	 * Update's the virtual IBM PC BIOS
	 * @see ibmpc.devices.memory.IbmPcRandomAccessMemory#updateVirtualBIOS()
	 */
	void updateVirtualBIOS( IbmPcDisplayContext dc );
	
	/**
	 * Writes the given character at the given (x,y) coordinates 
	 * @param x the given x-coordinate
	 * @param y the given y-coordinate
	 * @param ch the given character
	 * @param colorIndex the given color index
	 */
	void drawCharacter( IbmPcDisplayContext dc, int x, int y, char ch );
	
	/**
	 * Writes a pixel to (x,y) having the given color
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param x the given x-coordinate
	 * @param y the given y-coordinate
	 * @param colorIndex the given color index
	 */
	void drawPixel( IbmPcDisplayContext dc, int x, int y, int colorIndex );
	
	/**
	 * Writes the given character to the given display page having the 
	 * the default color attribute.
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param displayPage the given display page
	 * @param character the character
	 * @param moveCursor indicates whether the cursor should 
	 * be moved from it's current position. 
	 * @return the new cursor position
	 */
	int writeCharacter( IbmPcDisplayContext dc, int displayPage, int character, boolean moveCursor );
	
	/**
	 * Writes the given character to the given display page having the 
	 * the given color attribute.
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param displayPage the given display page
	 * @param character the character
	 * @param attribute the given color attribute
	 * @param moveCursor indicates whether the cursor should 
	 * be moved from it's current position. 
	 * @return the new cursor position
	 */
	int writeCharacter( IbmPcDisplayContext dc, int displayPage, int character, int attribute, boolean moveCursor );
	
	/**
	 * Writes the given content to the video memory at the current position of
	 * the virtual cursor
	 * @param dc the given {@link IbmPcDisplayContext display context}
	 * @param position the location of the text on the screen
	 * @param text the given content to display
	 */
	int writeText( IbmPcDisplayContext dc, int position, String text );
	
	/////////////////////////////////////////////////////
	//      Caption-based Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * @return the current caption text 
	 */
	String getCaption();
	
	/**
	 * @return the number of rows [on screen] that
	 * are being consumed by the caption (if set)
	 */
	int getCaptionRows();
	
	/**
	 * Sets the caption to the given text, or removes if the caption
	 * if it is <tt>null</tt>
	 * @param caption the given caption text, or <tt>null</tt> for no caption
	 */
	void setCaption( String caption );
	
	/**
	 * Indicates whether a caption is currently set
	 * @return true, if a caption is currently set
	 */
	boolean hasCaption();
	
	/////////////////////////////////////////////////////
	//      Accessor Method(s)
	/////////////////////////////////////////////////////

	/**
	 * @return the width of the display in pixels
	 */
	int getColumns();

	/**
	 * @return the height of the display in pixels
	 */
	int getHeight();

	/**
	 * @return the segment in memory for this mode
	 */
	int getMemorySegment();
	
	/**
	 * @return Returns the memorySize.
	 */
	int getMemorySize();

	/**
	 * @return the number of rows
	 */
	int getRows();

	/**
	 * @return the column width
	 */
	int getWidth();

	/**
	 * @return the number of available colors
	 */
	int getColors();
	
	/**
	 * @return Returns the fontWidth.
	 */
	int getFontWidth();	
	/**
	 * @return Returns the fontHeight.
	 */
	int getFontHeight();
	
	/**
	 * @return the number of available video pages
	 */
	int pages();
	
	/**
	 * @return the size of a single video page
	 */
	int getPageSize();
	
	/**
	 * @return the current BIOS video mode number
	 */
	int getVideoMode();
	
	/** 
	 * Indicates whether this mode is graphics-capable
	 * @return true, if this mode is graphics-capable
	 */
	boolean isGraphical();
	
}
