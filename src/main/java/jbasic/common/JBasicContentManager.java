package jbasic.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * JBasic Content Manager
 * @author lawrence.daniels@gmail.com
 */
public class JBasicContentManager {
	private static final String FONTS_8X8 = "resources/jbasic.fnt";

	///////////////////////////////////////////////////////
	//      Service Method(s)
	///////////////////////////////////////////////////////
	
	/**
	 * Returns the CGA/EGA 8x8 font data
	 * @return the CGA/EGA 8x8 font data
	 * @throws IOException
	 */
	public static byte[] get8x8FontData() 
	throws IOException {
		// get a URL to the resource
		final URL url = lookupResource( FONTS_8X8 );

		// get the font data
		final byte[] fontdata = getResourceContent( url );

		// return the font data
		return fontdata;	
	}
	
	///////////////////////////////////////////////////////
	//      Internal Service Method(s)
	///////////////////////////////////////////////////////
	
	/**
	 * Looks up the given resource in both the Jar File 
	 * (if the application was loaded from one) and the local
	 * file system.
	 * @param resourcePath the path of the resource
	 * @return the URL to the resource or 
	 * <code>null</code> if it could not be found
	 * @throws MalformedURLException
	 */
	private static URL lookupResource( final String resourcePath ) 
	throws MalformedURLException {
		// lookup the resource (within the JAR)
		URL url = resourcePath.getClass().getResource( resourcePath );
		if( url != null ) return url;
		
		// now, let's check the file system
		final File file = new File( resourcePath );
		if( file.exists() ) return file.toURL();
		
		// couldn't find it
		return null;
	}
	
	/**
	 * Returns the context read from the URL
	 * @param url the given URL
	 * @return the context read from the URL
	 * @throws IOException
	 */
	private static byte[] getResourceContent( final URL url ) 
	throws IOException {
		InputStream in = null;
		try {
			// open the stream
			in = url.openStream();
			
			// create a buffer for reading the data
			final byte[] buffer = new byte[1024];
			
			// read the content from the stream
			ByteArrayOutputStream baos = new ByteArrayOutputStream( 1024 );
			int count;
			while( ( count = in.read( buffer ) ) != -1 ) {
				baos.write( buffer, 0, count );
			}
			
			// return the byte data
			return baos.toByteArray();
		}
		finally {
			if( in != null ) in.close();
		}
	}
	
}
