package org.ldaniels528.javapc.jbasic.app.ide;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import static java.lang.String.format;

/**
 * JBasic Image Management Services 
 * @author lawrence.daniels@gmail.com
 */
public class JBasicImageManager {
	// explorer icons
	public static final Icon OPCODE 		= getImage( "opCode.png" );
	public static final Icon PROGRAM 		= getImage( "program.png" );
	
	// edit menu icons
	public static final Icon CUT 			= getImage( "menu/Cut16.gif" );
	public static final Icon COPY 			= getImage( "menu/Copy16.gif" );
	public static final Icon PASTE 			= getImage( "menu/Paste16.gif" );
	
	// file menu icons
	public static final Icon NEW 			= getImage( "menu/New16.gif" );
	public static final Icon OPEN 			= getImage( "menu/Open16.gif" );
	public static final Icon SAVE 			= getImage( "menu/Save16.gif" );
	public static final Icon SAVE_ALL		= getImage( "menu/SaveAll16.gif" );
	public static final Icon SAVE_AS 		= getImage( "menu/SaveAs16.gif" );
	
	// program menu icons
	public static final Icon COMPILE 		= getImage( "menu/Compile.png" );
	public static final Icon DEBUG 			= getImage( "menu/Debug.png" );
	public static final Icon RUN	 		= getImage( "menu/Run.png" );
	public static final Icon STOP	 		= getImage( "menu/Stop16.gif" );

	private static final Logger logger = Logger.getLogger(JBasicImageManager.class);

	/**
	 * Loads an image from the file
	 * @param imageName the name of the image
	 * @return the loaded {@link ImageIcon image}
	 */
	private static Icon getImage( String imageName ) {
		// get the image file
		final File imageFile = new File( System.getProperty( "user.dir" ) + 
										File.separator + 
										"resources/Images"  +
										File.separator +
										imageName );
		try {
			// load the image
			return new ImageIcon( imageFile.toURI().toURL() );
		}
		catch( MalformedURLException e ) {
			logger.debug(format("Couldn't load Loading %s...", imageFile.getAbsolutePath()));
			e.printStackTrace();
			return null;
		}
	}

}
