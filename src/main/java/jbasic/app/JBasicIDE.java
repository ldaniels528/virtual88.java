package jbasic.app;

import jbasic.app.ide.JBasicIDEFrame;
import jbasic.app.ide.JBasicIdeContext;

/**
 * Represents a PowerBASIC compatible integrated 
 * development environment (IDE).
 * @author lawrence.daniels@gmail.com
 */
public class JBasicIDE {
	public static final float VERSION = 0.421f;		
	
	/**
	 * Default constructor
	 */
	public JBasicIDE() {
		super();
	}
	
	/**
	 * Provides stand alone execution
	 * @param args the given commandline arguments
	 */
	public static void main( String[] args ) {
		JBasicIDE app = new JBasicIDE();
		app.run();
	}
	
	/**
	 * Starts the IDE
	 */
	public void run() {
		new JBasicIDEFrame( "JBasicIDE v" + VERSION, new JBasicIdeContext() );
	}
	
}
