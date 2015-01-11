package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file;

import java.io.File;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.storage.GwBasicStorageDevice;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * Save Program Command (SAVE)
 * @author lawrence.daniels@gmail.com
 */
public class SaveOp extends AbstactFileOpCode {
	private static final String EXTENSION = "bas";

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
   */
  public SaveOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
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
	  
	  // save the file
	  storage.save( program, theFile );
  }

}
