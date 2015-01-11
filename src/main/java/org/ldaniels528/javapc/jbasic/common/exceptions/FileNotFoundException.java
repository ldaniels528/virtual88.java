package org.ldaniels528.javapc.jbasic.common.exceptions;

import java.io.File;


/**
 * Represents a GWBASIC "File not found" Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class FileNotFoundException extends JBasicException {

	public FileNotFoundException( File file ) {
		super( "File not found" );
	}
	
}
