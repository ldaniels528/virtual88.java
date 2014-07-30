package ibmpc.devices.display;

/**
 * IBM PC Color Set
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcColorSet {
	private int foreground;
	private int background;
	private int border;
	
	/**
	 * Creates an instance of this color set
	 * @param colors the given array of colors
	 */
	public IbmPcColorSet( final int ... colors ) {
		switch( colors.length ) {		
			case 3: border = colors[2];
			case 2: background = colors[1];
			case 1: foreground = colors[0]; break;
		}
	}
	
	/**
	 * @return the text color attribute
	 */
	public byte asAttribute() {
		return (byte)( ( background << 4 ) | foreground );
	}
	
	/**
	 * @return Returns the background.
	 */
	public int getBackground() {
		return background;
	}
	
	/**
	 * @return Returns the border.
	 */
	public int getBorder() {
		return border;
	}
	
	/**
	 * @return Returns the foreground.
	 */
	public int getForeground() {
		return foreground;
	}
	
	/**
	 * Updates the environment with the given color set
	 * @param colorSet the given {@linkp {@link IbmPcColorSet color set}
	 */
	public void update( final IbmPcColorSet colorSet ) {
		if( colorSet.foreground != -1 ) this.foreground = colorSet.foreground;
		if( colorSet.background != -1 ) this.background = colorSet.background;
		if( colorSet.border != -1 ) this.border = colorSet.border;
	}

}
