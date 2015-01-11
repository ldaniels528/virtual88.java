package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import java.io.File;
import java.io.IOException;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * Generic File OpCode
 * @author lawrence.daniels@gmail.com
 */
public abstract class AbstactFileOpCode extends GwBasicCommand {
  protected Value sourcePath;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
   */
  public AbstactFileOpCode( TokenIterator it ) throws JBasicException {
	  parse( it );
  }
  
  /**
   * Returns a reference to the source/target file
   * @param extension the default three character extension, if one is not present 
   * (i.e. "BAS"); this argument may be <code>null</code>
   * @param program the given {@link JBasicSourceCode program} instance
   * @param environment the given {@link IbmPcSystem environment}
   * @return a reference to the source/target {@link File file}
   * @throws JBasicException 
   */
  public File getFileReference( final Value filePath, 
		  					    final String extension, 
		  					    final JBasicCompiledCode program, 
		  					    final IbmPcSystem environment ) 
  throws JBasicException {
	  // get the object value
	  final MemoryObject object = filePath.getValue( program );
	  
	  // make sure it's a string
	  if( !object.isString() )
		  throw new TypeMismatchException( object );	 
	  
	  // get the storage device
	  final IbmPcStorageSystem storage = environment.getStorageSystem();
	  
	  // determine the full path of the file
	  final StringBuilder thePath;
	  try {
		thePath = new StringBuilder( storage.getCurrentDirectory().getCanonicalPath() )
		  								.append( File.separator )
		  								.append( object.toString() );
	  } catch( final IOException e ) {
		  throw new JBasicException( e );
	  }
	  
	  // add default extension?
	  if( extension != null && thePath.indexOf( "." ) == -1 ) {
		  thePath.append( '.' ).append( extension );
	  }
 
	  // return the file object
	  return new File( thePath.toString() );
  }
  
  /**
   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  protected void parse( TokenIterator it ) throws JBasicException {
	  // create a value from the raw content
	  sourcePath = GwBasicValues.getValue( it );

	  // there should be no more elements
	  JBasicTokenUtil.noMoreTokens( it );
  }

}
