package jbasic.gwbasic.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import msdos.storage.MsDosStorageSystem;

import jbasic.common.exceptions.FileNotFoundException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicProgramStatement;
import jbasic.common.program.JBasicSourceCode;
import jbasic.gwbasic.program.GwBasicStatement;
import jbasic.gwbasic.values.GwBasicValues;
import ibmpc.devices.memory.OutOfMemoryException;

/**
 * GWBASIC/BASICA Storage Device
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicStorageDevice extends MsDosStorageSystem {
	
	/**
	 * Loads a program into the given environment
	 * @param filePath the given {@link File file path}
	 * @throws JBasicException
	 * @throws OutOfMemoryException
	 */
	public void load( final JBasicSourceCode program, final File file )
	throws JBasicException, OutOfMemoryException {
		// load the new program
		BufferedReader reader = null;
		try {
			// open the file using a reader
			reader = new BufferedReader( new FileReader( file ) );

			// clear the current program
			program.clear();

			// load the data
			String line = null;
			while( ( line = reader.readLine() ) != null ) {
				line = trim(line);
				int index = line.indexOf(' ');
				if( index != -1 ) {
					int lineNo = GwBasicValues.parseIntegerString( line.substring( 0, index ) );
					String text = line.substring( index, line.length() );
					program.add( new GwBasicStatement( lineNo, text) );
				}
			}
		} 
		catch( final java.io.FileNotFoundException e ) {
			throw new FileNotFoundException( file );
		} 
		catch( final IOException e ) {
			throw new JBasicException( e );
		} 
		finally {
			try { reader.close(); } catch ( final Exception e ) { }
		}
	}

	/**
	 * Saves the current program into the given environment
	 * 
	 * @param filePath
	 *            the given {@link File file path}
	 * @throws JBasicException
	 */
	public void save( final JBasicSourceCode program, final File file )
	throws JBasicException {
		// get the contents of the program
		Collection<JBasicProgramStatement> statements = program.getStatements();

		// save the program
		BufferedWriter writer = null;
		try {
			// write the file to disk
			writer = new BufferedWriter(new FileWriter(file));
			for (JBasicProgramStatement statement : statements) {
				writer.write(statement.toString());
				writer.newLine();
			}
			writer.flush();
		} catch (Exception e) {
			throw new JBasicException(e);
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Trims the given string
	 * @param s the given string
	 * @return a trimmed version of the given string
	 */
	private String trim( final String s ) {
		final StringBuilder sb = new StringBuilder( s.trim() );
		while( sb.length() > 0 && sb.charAt(0) == ' ' ) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

}
