package org.ldaniels528.javapc.jbasic.common;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColorSet;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * Represents a BASICA/GWBASIC/QBASIC display wrapper.
 * @author lawrence.daniels@gmail.com
 */
public class JBasicDisplayWrapper implements IbmPcDisplay {
	private final IbmPcDisplay display;
	private final JBasicKeyLabels keyLabels;

	/////////////////////////////////////////////////////
	//      Constructor(s)
	/////////////////////////////////////////////////////

	/**
	 * Creates an instance of this virtual screen
	 * @param memory the given {@link IbmPcRandomAccessMemory random access memory}
	 * @param mode the given {@link IbmPcDisplayMode mode}
	 */
	public JBasicDisplayWrapper( final IbmPcDisplay display,
		  			   	    	 final IbmPcDisplayMode mode,
		  			   	    	 final JBasicKeyLabels keyLabels ) {	
		this.display   = display;    
		this.keyLabels = keyLabels;
		setDisplayMode( mode );
	}

	/////////////////////////////////////////////////////
	//      Service Methods
	/////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#backspace()
	 */
	public void backspace() {
		display.backspace();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#clear()
	 */
	public void clear() {
		display.clear();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#copyPage(int, int)
	 */
	public void copyPage(int sourcePage, int targetPage) {
		display.copyPage( sourcePage, targetPage );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#drawLine(int, int, int, int, int)
	 */
	public void drawLine(int x1, int y1, int x2, int y2, int colorIndex) {
		display.drawLine(x1, y1, x2, y2, colorIndex);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getActivePage()
	 */
	public int getActivePage() {
		return display.getActivePage();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getCharacterAttribute(int)
	 */
	public int getCharacterAttribute(int offset) {
		return display.getCharacterAttribute( offset );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getContext()
	 */
	public IbmPcDisplayContext getContext() {
		return display.getContext();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getCursorPosition()
	 */
	public int getCursorPosition() {
		return display.getCursorPosition();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getCursorPosition(int)
	 */
	public int getCursorPosition(int displayPage) {
		return display.getCursorPosition( displayPage );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getDisplayMode()
	 */
	public IbmPcDisplayMode getDisplayMode() {
		return display.getDisplayMode();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#getVideoOffset(int, int)
	 */
	public int getVideoOffset(int column, int row) {
		return display.getVideoOffset(column, row);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#init(org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame)
	 */
	public void init(IbmPcDisplayFrame frame) {
		display.init( frame );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#newLine()
	 */
	public void newLine() {
		display.newLine();
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#readPixel(int, int)
	 */
	public int readPixel(int x, int y) {
		return display.readPixel(x, y);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#scroll(int)
	 */
	public void scroll(int nRows) {
		display.scroll(nRows);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#setActivePage(int)
	 */
	public void setActivePage(int page) {
		display.setActivePage(page);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#setCharacterAttribute(int, int)
	 */
	public void setCharacterAttribute(int offset, int charAttr) {
		display.setCharacterAttribute(offset, charAttr);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#setColor(org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColorSet)
	 */
	public void setColor(IbmPcColorSet colorSet) {
		display.setColor(colorSet);
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#setCursorPosition(int, int)
	 */
	public void setCursorPosition(int column, int row) {
		display.setCursorPosition(column, row);
	}

	/////////////////////////////////////////////////////
	//      Service Methods
	/////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.IbmPcVgaColorDisplay#setMode(IbmPcDisplayMode)
	 */
	public void setDisplayMode( final IbmPcDisplayMode mode ) {
		keyLabels.setColumns( mode.getColumns() );
		mode.setCaption( keyLabels.getLabelText() );
		display.setDisplayMode( mode );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#update()
	 */
	public void update() {
		display.update();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#writeCharacter(int, int, boolean)
	 */
	public void writeCharacter( final int displayPage, 
								final int character, 
								final boolean moveCursor) {
		display.writeCharacter( displayPage, character, moveCursor );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#writeCharacter(int, int, int, boolean)
	 */
	public void writeCharacter( final int displayPage, 
								final int character, 
								final int attribute, 
								final boolean moveCursor) {
		display.writeCharacter( displayPage, character, attribute, moveCursor );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#write(java.lang.String)
	 */
	public void write( final String text) {
		display.write( text );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#writeLine(java.lang.String)
	 */
	public void writeLine( final String text ) {
		display.writeLine( text );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#writePixel(int, int, int)
	 */
	public void writePixel( final int x, final int y, final int colorIndex) {
		display.writePixel( x, y, colorIndex );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#writeXY(int, int, java.lang.String)
	 */
	public void writeXY( final int col, final int row, final String text) {
		display.writeXY( col, row, text );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay#setCursorPosition(int)
	 */
	public void setCursorPosition( final int position ) {
		display.setCursorPosition( position );
	}
     
}