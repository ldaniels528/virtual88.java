package jbasic.gwbasic.program.commands.io.file;

import ibmpc.system.IbmPcSystem;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;

import java.io.File;

import jbasic.common.tokenizer.TokenIterator;

/**
 * Remove Directory Command (RMDIR)
 */
public class RmdirOp extends AbstactFileOpCode {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws jbasic.common.exceptions.JBasicException
   */
  public RmdirOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /**
   * Executes this {@link jbasic.common.program.OpCode opCode}
 * @throws jbasic.common.exceptions.JBasicException
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
	  // get the environment
	  final IbmPcSystem environment = program.getSystem();
	  
	  // determine the full path of the file
	  final File directory = getFileReference( sourcePath, null, program, environment );
		
	  // create the directory
	  directory.delete();
  }

}