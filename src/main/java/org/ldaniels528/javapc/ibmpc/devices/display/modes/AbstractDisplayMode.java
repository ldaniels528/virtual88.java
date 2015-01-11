package org.ldaniels528.javapc.ibmpc.devices.display.modes;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayException;

/**
 * Represents a Generic Display Mode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstractDisplayMode implements IbmPcDisplayMode {
	protected final boolean graphical;
	protected final int videoMode;
	protected final int memorySegment;
	protected final int memorySize;
	protected final int rows;
	protected final int columns;	
	protected final int width;
	protected final int height;
	protected final int fontWidth;
	protected final int fontHeight;
	protected final int pageSize;
	protected final int pages;
	protected final int colors;
	protected String caption;

	/////////////////////////////////////////////////////
	//      Constructor(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Creates an instance of a IBM PC display mode
	 */
	public AbstractDisplayMode( final int videoMode, 
								final int memorySegment, 
								final int columns, 
								final int rows, 
							    final int width, 
							    final int height, 
							    final int fontWidth, 
							    final int fontHeight, 
							    final int colors, 
							    final int pageSize, 
							    final int memorySize, 
							    final boolean graphical ) {		
		this.videoMode		= videoMode;
		this.memorySegment	= memorySegment;
		this.columns 		= columns;		
		this.rows 			= rows;		
		this.width 			= width;
		this.height 		= height;
		this.fontWidth		= fontWidth;
		this.fontHeight		= fontHeight;
		this.colors  		= colors;				
		this.memorySize		= memorySize;		
		this.pageSize		= pageSize; 
		this.pages			= pageSize / memorySize;
		this.graphical		= graphical;
		this.caption		= null;
	}
	
	/////////////////////////////////////////////////////
	//      Service Method(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Copies the source page to the target page
	 * <br><b>NOTE</b>: Page #1 is index 0
	 * @param sourcePage the given source page
	 * @param targetPage the given target page
	 * @throws IbmPcDisplayException
	 */
	public void copyPage( final IbmPcDisplayContext dc, final int sourcePage, final int targetPage ) {	
		// check the source page
		if( sourcePage < 0 || sourcePage >= pages ) {
			throw new IbmPcDisplayException( "Source page is out of range" );
		}
		
		// check the target page
		if( targetPage < 0 || targetPage >= pages ) {
			throw new IbmPcDisplayException( "Target page is out of range" );
		}
		
		// get the offsets the of the source and target pages
		final int sourceOffset = sourcePage * pageSize;
		final int targetOffset = targetPage * pageSize;
		  
		// retrieve the source page
		final byte[] block = dc.memory.getBytes( memorySegment, sourceOffset, pageSize );
		  
		// write the source page to the target
		dc.memory.setBytes( memorySegment, targetOffset, block, pageSize );
	}
	
	/** 
	 * Update's the virtual IBM PC BIOS
	 */
	public final void updateVirtualBIOS( final IbmPcDisplayContext dc ) {
		dc.bios.updateVideoInfo( this, dc );
	}

	/////////////////////////////////////////////////////
	//      Caption-based Method(s)
	/////////////////////////////////////////////////////
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#hasCaption()
	 */
	public boolean hasCaption() {
		return ( caption != null );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#getCaption()
	 */
	public String getCaption() {
		return caption;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#getCaptionRows()
	 */
	public int getCaptionRows() {
		return hasCaption() ? 1 : 0;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.display.modes.IbmPcDisplayMode#setCaption(java.lang.String)
	 */
	public void setCaption( final String caption ) {
		this.caption = caption;
	}
	
	/////////////////////////////////////////////////////
	//      Accessor Method(s)
	/////////////////////////////////////////////////////

	/**
	 * @return the width of the display in pixels
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @return the height of the display in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the segment in memory for this mode
	 */
	public int getMemorySegment() {
		return memorySegment;
	}
	
	/**
	 * @return Returns the memorySize.
	 */
	public int getMemorySize() {
		return memorySize;
	}

	/**
	 * @return the number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return the column width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the number of available colors
	 */
	public int getColors() {
		return colors;
	}
	
	/**
	 * @return Returns the fontWidth.
	 */
	public int getFontWidth() {
		return fontWidth;
	}
	
	/**
	 * @return Returns the fontHeight.
	 */
	public int getFontHeight() {
		return fontHeight;
	}
	
	/**
	 * @return the number of available video pages
	 */
	public int pages() {
		return pages;
	}
	
	/**
	 * @return the size of a single video page
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * @return the current BIOS video mode number
	 */
	public int getVideoMode() {
		return videoMode;
	}
	
	/** 
	 * Indicates whether this mode is graphics-capable
	 * @return true, if this mode is graphics-capable
	 */
	public boolean isGraphical() {
		return graphical;
	}
	
}
