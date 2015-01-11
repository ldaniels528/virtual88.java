package jbasic.gwbasic.program.commands.io.file;

import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.exceptions.IbmPcException;
import ibmpc.system.IbmPcSystem;

import java.io.File;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;

/**
 * Change Directory (CHDIR) Command
 */
public class ChdirOp extends AbstactFileOpCode {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws jbasic.common.exceptions.JBasicException
   */
  public ChdirOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the environment
	  final IbmPcSystem environment = program.getSystem();
		
	  // get the directory reference
	  final File directory = getFileReference( sourcePath, null, program, environment );
	  
	  // get the storage device
	  final IbmPcStorageSystem storage = environment.getStorageSystem();
	  
	  // set it as the new current directory
	  try {
		storage.setCurrentdirectory( directory );
	  } 
	  catch( final IbmPcException e ) {
		throw new JBasicException( e );
	  }
  }

}