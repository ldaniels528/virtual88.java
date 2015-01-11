package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file;

import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;

import java.io.File;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * Make Directory (MKDIR) Command
 */
public class MkdirOp extends AbstactFileOpCode {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public MkdirOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /**
   * Executes this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
 * @throws JBasicException
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
	  // get the environment
	  final IbmPcSystem environment = program.getSystem();
	  
	  // determine the full path of the file
	  final File directory = getFileReference( sourcePath, null, program, environment );
    
	  // create the directory
	  directory.mkdirs();
  }

}
