package jbasic.gwbasic.program.commands.io.file;

import java.io.File;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.JBasicSourceCode;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.storage.GwBasicStorageDevice;

import jbasic.common.tokenizer.TokenIterator;

/**
 * Load Program Command
 * <br>Syntax: LOAD <i>filename<i>
 * @author lawrence.daniels@gmail.com
 */
public class LoadOp extends AbstactFileOpCode {
	private static final String EXTENSION = "bas";

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public LoadOp( final TokenIterator it ) 
  throws JBasicException {
    super( it );
  }

  /**
   * Executes this {@link jbasic.common.program.OpCode opCode}
   * @param code the given {@link JBasicCompiledCode program}
   * @throws jbasic.common.exceptions.JBasicException
   */
  public void execute( final JBasicCompiledCode code ) 
  throws JBasicException {
	  // get the environment
	  final GwBasicEnvironment environment = (GwBasicEnvironment)code.getSystem();
	  
	  // determine the full path of the file
	  final File theFile = getFileReference( sourcePath, EXTENSION, code, environment );
	  
	  // get the storage device
	  final GwBasicStorageDevice storage = (GwBasicStorageDevice)environment.getStorageSystem();
	  
	  // get the memory resident program
	  final JBasicSourceCode program = environment.getProgram();
	  
	  // load the file
	  storage.load( program, theFile );	  
  }

}
