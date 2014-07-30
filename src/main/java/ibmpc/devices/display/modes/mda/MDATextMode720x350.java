/**
 * 
 */
package ibmpc.devices.display.modes.mda;

import ibmpc.devices.display.modes.AbstractTextMode;

/**
 * Represents the MDA 80x25 (720x350 resolution; 9x14 Font) Text Mode
 * @author lawrence.daniels@gmail.com
 */
public class MDATextMode720x350 extends AbstractTextMode {
	private static final int MEMORY_SEGMENT 	= 0xB000;
	private static final int RES_WIDTH   		= 720;
	private static final int RES_HEIGHT  		= 350;
	private static final int COLUMNS  			= 80;
	private static final int ROWS	  			= 25;
	private static final int FONT_WIDTH			= 9;
	private static final int FONT_HEIGHT		= 14;
	private static final int COLORS  			= 3;

	/**
	 * Default constructor
	 */
	public MDATextMode720x350() {
		super( 0x07, MEMORY_SEGMENT, COLUMNS, ROWS, RES_WIDTH, RES_HEIGHT, FONT_WIDTH, FONT_HEIGHT, COLORS );
	}
	
}