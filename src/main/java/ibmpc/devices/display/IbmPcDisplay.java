package ibmpc.devices.display;

import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.exceptions.IbmPcException;

/**
 * Represents an IBM PC-Style Virtual Display 
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcDisplay {
	
	/////////////////////////////////////////////////////
	//      Graphics/Text Common Method(s)
	/////////////////////////////////////////////////////

	/**
	 * Performs a backspace operation
	 */
	void backspace();

	/**
	 * Clears the virtual screen
	 */
	void clear();

	/**
	 * Initializes this display
	 * @param frame the given {@link IbmPcDisplayFrame frame}
	 */
	void init( IbmPcDisplayFrame frame );
	
	/**
	 * @return the current active display page
	 */
	int getActivePage();

	/**
	 * Sets the active display pahe
	 * @param page the given display page index
	 */
	void setActivePage(int page);

	/**
	 * Sets the given colors
	 * @param colorSet the given {@link IbmPcColorSet color set}
	 */
	void setColor( IbmPcColorSet colorSet );
	
	/**
	 * Returns the context for the display
	 * @return the {@link IbmPcDisplayContext context} for the display
	 */
	IbmPcDisplayContext getContext();
	
	/**
	 * Copies the source page to the target page
	 * @param sourcePage the given source page
	 * @param targetPage the given target page
	 */
	void copyPage( int sourcePage, int targetPage );

	/**
	 * @return the offset in memory where the virtual cursor currently resides
	 */
	int getCursorPosition();
	
	/**
	 * @return the offset in memory where the virtual cursor currently resides
	 */
	int getCursorPosition( int displayPage );
	
	/**
	 * Sets the virtual cursor to the given video offset position
	 * @param position the given video offset position
	 */
	void setCursorPosition( int position );

	/**
	 * Sets the virtual cursor to the given row and column
	 * @param column the given column
	 * @param row the given row
	 */
	void setCursorPosition( int column, int row );
	
	/**
	 * @return the current active display mode
	 */
	IbmPcDisplayMode getDisplayMode();

	/**
	 * Sets the row and column width of the screen
	 * @param mode the given {@link IbmPcDisplayMode display mode}
	 */
	void setDisplayMode( IbmPcDisplayMode mode );
	
	/**
	 * Forces the virtual cursor to the next line
	 */
	void newLine();

	/**
	 * Scrolls video memory up by the given number of rows
	 * @param nRows the given number of rows
	 */
	void scroll( int nRows );

	/**
	 * Updates the virtual screen
	 */
	void update();
	
	/**
	 * Writes the given character to the given display page having the 
	 * the default color attribute.
	 * @param displayPage the given display page
	 * @param character the character
	 * @param moveCursor indicates whether the cursor should 
	 * be moved from it's current position. 
	 */
	void writeCharacter( int displayPage, int character, boolean moveCursor );
	
	/**
	 * Writes the given character to the given display page having the 
	 * the given color attribute.
	 * @param displayPage the given display page
	 * @param character the character
	 * @param attribute the given color attribute
	 * @param moveCursor indicates whether the cursor should 
	 * be moved from it's current position. 
	 */
	void writeCharacter( int displayPage, int character, int attribute, boolean moveCursor );
	
	/**
	 * Writes the given content to the video memory at the current position of
	 * the virtual cursor
	 * @param text the given content to display
	 */
	void write( String text );

	/**
	 * Writes the given content to the video memory at the current position of
	 * the virtual cursor; then moves the beginning of the next line
	 * @param text the given content to display
	 */
	void writeLine( String text );
	
	/**
	 * Writes the given content to the video memory at the the given
	 * column and row positions. <b>NOTE:</b> the virtual cursor does
	 * not move.
	 * @param col the column of the on-screen position
	 * @param row the row of the on-screen position
	 * @param text the given content to display
	 */
	void writeXY( int col, int row, String text );

	/////////////////////////////////////////////////////
	//      Text-mode Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Reads the character (and attribute; if in text mode) 
	 * from the given offset.
	 * @param offset the given offset in memory
	 * @return the character
	 */
	int getCharacterAttribute( int offset );
	
	/**
	 * Writes the character from the given offset
	 * @param offset the given offset in memory
	 * @param charAttr the character or attribute
	 */
	void setCharacterAttribute( int offset, int charAttr );
	
	/**
	 * Calculates the video memory offset for the given row and column values
	 * @param column the given column value
	 * @param row the given row value
	 * @return the calculated video memory offset
	 */
	int getVideoOffset( int column, int row );
	
	/////////////////////////////////////////////////////
	//      Graphics Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Draws a line from (x1,y1) to (x2,y2) 
	 */
	void drawLine( int x1, int y1, int x2, int y2, int colorIndex );
	
	/**
	 * Draws a pixel at (x,y) 
	 */
	void writePixel( int x, int y, int colorIndex );
	
	/**
	 * Reads a pixel from (x,y) 
	 * @throws IbmPcException
	 */
	int readPixel( int x, int y );
	
}