package ibmpc.devices.display;

import java.awt.Color;

/**
 * IBM PC Basic Color Definitions 
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcColors {
	  // dark colors
	  public static final Color BLACK 		= new Color( 0x00, 0x00, 0x00 ); 
	  public static final Color BLUE		= new Color( 0x00, 0x00, 0xA8 );
	  public static final Color GREEN		= new Color( 0x00, 0xA8, 0x00 );
	  public static final Color CYAN		= new Color( 0x00, 0xA8, 0xA8 );
	  public static final Color RED			= new Color( 0xA8, 0x00, 0x00 );
	  public static final Color MAGENTA		= new Color( 0xA8, 0x00, 0xA8 );
	  public static final Color BROWN 		= new Color( 0xA8, 0x54, 0x00 );
	  public static final Color GRAY		= new Color( 0xA8, 0xA8, 0xA8 );
	  
	  // light colors
	  public static final Color DARK_GRAY 	= new Color( 0x54, 0x54, 0x54 );
	  public static final Color LIGHT_BLUE 	= new Color( 0x54, 0x54, 0xFE );
	  public static final Color LIGHT_GREEN	= new Color( 0x54, 0xFE, 0x54 );
	  public static final Color LIGHT_CYAN 	= new Color( 0x54, 0xFE, 0xFE );	  	 
	  public static final Color PINK 		= new Color( 0xFE, 0x54, 0x54 );
	  public static final Color LIGHT_MAGENTA= new Color( 0xFE, 0x54, 0xFE );
	  public static final Color YELLOW 		= new Color( 0xFE, 0xFE, 0x54 );
	  public static final Color WHITE 		= new Color( 0xFE, 0xFE, 0xFE );
	  
	  // define an array of 16 colors	  
	  public static final Color[] COLORS_16 = {
		  // dark colors
		  BLACK, BLUE, GREEN, CYAN, RED, MAGENTA, BROWN, GRAY,		  
		  // light colors
		  DARK_GRAY, LIGHT_BLUE, LIGHT_GREEN, LIGHT_CYAN, PINK, 
		  LIGHT_MAGENTA, YELLOW, WHITE
	  };
	  
	  // define an array of 256 colors
	  public static final Color[] COLORS_256 = generate256Colors();
	  
	  /**
	   * Returns the color that correspond to the given color parameter
	   * @param param the given color parameter
	   * @return the {@link Color color} that correspond to the given color parameter
	   */
	  public static Color getColor( final Integer param ) {
		  return ( param != null ) ? COLORS_16[ param ] : null; 
	  }
	  
	  /**
	   * Returns the colors that correspond to the given color parameters
	   * @param params the given color parameters
	   * @return the {@link Color colors} that correspond to the given color parameters
	   */
	  public static Color[] getColors( final Integer ... params ) {
		  final Color[] colors = new Color[ params.length ];
		  for( int i = 0; i < colors.length; i++ ) {
			  colors[i] = getColor( params[i] );
		  }
		  return colors;
	  }
	  
		/**
		 * Generates the 256-colors used by this mode
		 * @return the 256-{@link Color colors} used by this mode
		 */
		private static Color[] generate256Colors() {
			final Color[] baseColors = IbmPcColors.COLORS_16;
			final Color[] colors = new Color[256];
			for( int n = 0; n < colors.length; n++ ) {
				final int intensity 	 = n % 16;
				final Color baseColor = baseColors[intensity];
				final int r = baseColor.getRed();
				final int g = baseColor.getGreen();
				final int b = baseColor.getBlue();
				colors[n] = new Color( ( r + intensity ) % 256, ( g + intensity ) % 256, ( b + intensity ) % 256 );
			}
			return colors;
		}

}
