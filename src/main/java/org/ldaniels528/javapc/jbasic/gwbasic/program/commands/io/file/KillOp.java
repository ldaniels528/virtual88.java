package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file;

import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import java.io.File;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.FileNotFoundException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;

/**
 * KILL <file>
 * @author lawrence.daniels@gmail.com
 */
public class KillOp extends AbstactFileOpCode {

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public KillOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the environment
	  final IbmPcSystem environment = program.getSystem();
	  
	  // determine the full path of the file
	  final File thePath = getFileReference( sourcePath, null, program, environment );
	  
	  // file must be found
	  if( !thePath.exists() )
		  throw new FileNotFoundException( thePath );
    
	  // create the directory
	  thePath.delete();
  }

}
