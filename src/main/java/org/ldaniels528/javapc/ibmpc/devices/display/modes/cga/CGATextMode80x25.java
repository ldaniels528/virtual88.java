/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.devices.display.modes.cga;

import org.ldaniels528.javapc.ibmpc.devices.display.modes.AbstractTextMode;

/**
 * Represents the CGA 80x25 Text Mode
 * @author lawrence.daniels@gmail.com
 */
public class CGATextMode80x25 extends AbstractTextMode {
	private static final int MEMORY_SEGMENT = 0xB800;
	private static final int RES_WIDTH   	= 640;
	private static final int RES_HEIGHT  	= 200;
	private static final int COLUMNS  		= 80;
	private static final int ROWS	  		= 25;
	private static final int FONT_WIDTH		= 8;
	private static final int FONT_HEIGHT	= 8;
	private static final int COLORS  		= 16;

	/**
	 * Default constructor
	 */
	public CGATextMode80x25() {
		super( 0x03, MEMORY_SEGMENT, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, FONT_WIDTH, FONT_HEIGHT, COLORS );
	}
	
}