/**
 * 
 */
package ibmpc.devices.display.fonts;

/**
 * Represents an IBM PC-based Font
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcFont {

	/**
	 * @return the width of this font
	 */
	int getWidth();
	
	/**
	 * @return the height of this font
	 */
	int getHeight();
	
	/**
	 * Returns the byte data for the given font
	 * @param font the given font (0-255)
	 * @return the byte data for the given font
	 */
	byte[] getData( int font );
	
}
