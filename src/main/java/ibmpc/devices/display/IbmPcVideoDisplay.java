package ibmpc.devices.display;

import ibmpc.devices.cpu.x86.bios.IbmPcBIOS;
import ibmpc.devices.display.modes.IbmPcDisplayMode;

import java.awt.Graphics2D;

/**
 * Represents an IBM PC Video Display capable 
 * of CGA, EGA, and VGA (Text and Graphics) Modes.
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcVideoDisplay implements IbmPcDisplay {
	protected final IbmPcDisplayContext dc;
	protected IbmPcDisplayMode mode;
	
	/**
	 * Creates an instance of this color display
	 * @param bios the given {@link IbmPcBIOS BIOS} instance
	 * @param mode the given {@link IbmPcDisplayMode display mode} instance
	 */
	public IbmPcVideoDisplay( final IbmPcBIOS bios,
						   	  final IbmPcDisplayMode mode ) {
		this.dc	= new IbmPcDisplayContext( bios );
		setDisplayMode( mode ); 
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#backspace()
	 */
	public void backspace() {
		mode.backspace( dc );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#clear()
	 */
	public void clear() {
		mode.clear( dc );
		dc.position = 0;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#init(ibmpc.display.IbmPcDisplayFrame)
	 */
	public void init( final IbmPcDisplayFrame frame ) {
		dc.frame = frame;		
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getActivePage()
	 */
	public int getActivePage() {
		return dc.activePage;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#setActivePage(int)
	 */
	public void setActivePage( final int page ) {
		dc.activePage = page;		
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#setColor(ibmpc.display.IbmPcColorSet)
	 */
	public void setColor( final IbmPcColorSet colorSet ) {
		dc.color = colorSet;		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getContext()
	 */
	public IbmPcDisplayContext getContext() {
		return dc;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#copyPage(int, int)
	 */
	public void copyPage( final int sourcePage, final int targetPage ) {
		mode.copyPage( dc, sourcePage, targetPage );		
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getCursorPosition()
	 */
	public int getCursorPosition() {
		return dc.activePage * mode.getPageSize() + dc.position;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getCursorPosition(int)
	 */
	public int getCursorPosition( final int displayPage ) {
		return displayPage * mode.getPageSize() + dc.position;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.display.IbmPcDisplay#setCursorPosition(int)
	 */
	public void setCursorPosition( final int position ) {
		dc.position = position;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#setCursorPosition(int, int)
	 */
	public void setCursorPosition( final int column, final int row ) {
		dc.position = mode.getCursorPosition( dc, column, row );		
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getDisplayMode()
	 */
	public IbmPcDisplayMode getDisplayMode() {
		return mode;
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#setDisplayMode(ibmpc.display.modes.IbmPcDisplayMode)
	 */
	public void setDisplayMode( final IbmPcDisplayMode newMode ) {
		this.mode = newMode;
		dc.activePage = 0;
		dc.position = 0;
		mode.clear( dc );
		mode.updateVirtualBIOS( dc );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#newLine()
	 */
	public void newLine() {
		mode.newLine( dc );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#scroll(int)
	 */
	public void scroll( final int nRows ) {
		mode.scroll( dc, nRows );		
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#update()
	 */
	public void update() {		
		if( dc.frame != null ) {
			// get the offscreen graphics context
			final Graphics2D offscreen = dc.frame.getOffScreen();
			
		    // copy the text in cache to screen
			mode.render( dc, offscreen );
			
		    // blit to the screen
		    dc.frame.blit();
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.display.IbmPcDisplay#writeCharacter(int, int, boolean)
	 */
	public void writeCharacter( final int displayPage, 
								final int character, 
								final boolean moveCursor ) {
		dc.position = mode.writeCharacter( dc, displayPage, character, moveCursor );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.display.IbmPcDisplay#writeCharacter(int, int, int, boolean)
	 */
	public void writeCharacter( final int displayPage, 
								final int character, 
								final int attribute, 
								final boolean moveCursor ) {
		dc.position = mode.writeCharacter( dc, displayPage, character, attribute, moveCursor );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#write(java.lang.String)
	 */
	public void write( final String text) {
		dc.position = mode.writeText( dc, dc.position, text );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#writeLine(java.lang.String)
	 */
	public void writeLine( final String text ) {
		mode.writeText( dc, dc.position, text );
		mode.newLine( dc );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#writeXY(int, int, java.lang.String)
	 */
	public void writeXY( int column, int row, String text ) {
		final int position = mode.getCursorPosition( dc, column, row );
		mode.writeText( dc, position, text );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getCharacterAttribute(int)
	 */
	public int getCharacterAttribute( final int offset ) {
		return mode.isGraphical()
  			? dc.memory.getByte( mode.getMemorySegment(), offset )
  			: dc.memory.getWord( mode.getMemorySegment(), offset );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#setCharacterAttribute(int, int)
	 */
	public void setCharacterAttribute( final int offset, final int charAttr ) {
		if( mode.isGraphical() ) {
			dc.memory.setByte( mode.getMemorySegment(), offset, (byte)charAttr );
		}
		else {
			dc.memory.setWord( mode.getMemorySegment(), offset, charAttr );		
		}
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#getVideoOffset(int, int)
	 */
	public int getVideoOffset( final int column, final int row) {
		return mode.getCursorPosition( dc, column, row );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#drawLine(int, int, int, int, int)
	 */
	public void drawLine( final int x1, final int y1, final int x2, final int y2, final int colorIndex ) {
		mode.drawLine( dc, x1, y1, x2, y2, colorIndex );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#writePixel(int, int, int)
	 */
	public void writePixel( final int x, final int y, final int colorIndex) {
		mode.drawPixel( dc, x, y, colorIndex );
	}

	/* (non-Javadoc)
	 * @see ibmpc.display.IbmPcDisplay#readPixel(int, int)
	 */
	public int readPixel( final int x, final int y ) {
		return mode.readPixel( dc, x, y );
	}

}
