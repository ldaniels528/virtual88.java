package jbasic.gwbasic.program.commands.io.file;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.system.IbmPcSystem;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

import java.io.File;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * Files Command 
 * <br><b>Syntax</b>: FILE [search-args]
 */
public class FilesOp extends GwBasicCommand {

	/**
	 * Creates an instance of this op code.
	 * @param it the given {@link TokenIterator iterator}
	 * @throws JBasicException
	 */
	public FilesOp( final TokenIterator it ) 
	throws JBasicException {
		// TODO: Files "C:\*.*" should be supported
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode program ) 
	throws JBasicException {
		// define how large each cell should be
		final int CELL_WIDTH = 17;
		
		// get the environment
		final IbmPcSystem environment = program.getSystem();
		
		// get the storage device
		final IbmPcStorageSystem storage = environment.getStorageSystem();
		
		// get the list of files
		final File[] files = storage.getFiles();

		// get an instance of the screen
		final IbmPcDisplay screen = environment.getDisplay();
		
		// get the current display mode
		final IbmPcDisplayMode mode = screen.getDisplayMode();

		// get the column width of the current display mode
		final int COLUMNS = mode.getColumns();

		// determine the number of filenames per line
		final int FILES_PER_LINE = COLUMNS / CELL_WIDTH;
		
		// display the files
		for( int n = 0; n < files.length; n++ ) {
			// determine where we are
			final int x = n % FILES_PER_LINE;
			
			// at the start of each new set create new line
			if( ( n > 0 ) && ( x == 0 ) ) {
				screen.newLine();
				screen.update();
			}
			
			// create the filename display
			final String filename = getFileName( files[n] );
			
			// write the filename to the screen
			screen.write( padFileName( filename, CELL_WIDTH ) );
		}
		
		// perform a final newline
		screen.newLine();
		screen.update();
	}
	
	/**
	 * Returns the display ready file name
	 * @param file the given {@link File file}
	 * @return the display ready file name
	 */
	private String getFileName( final File file ) {
		return file.isDirectory() ? '<' + file.getName() + '>' : file.getName();
	}
	
	/**
	 * Left justifies the given file name
	 * @param name the given file name
	 * @param columnWidth the width of the column
	 * @return a left justified file name
	 */
	private String padFileName( String name, int columnWidth ) {
		final StringBuilder sb = new StringBuilder( columnWidth );
		sb.append( name );
		while( sb.length() < columnWidth ) {
			sb.append( ' ' );
		}
		return sb.toString();
	}

}
