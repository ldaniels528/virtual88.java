package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file;

import org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import java.io.File;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/** 
 * NAME Command
 * <br>Purpose: To change the name of a disk file.
 * <br>Syntax: NAME <i>old filename</i> AS <i>new filename</i>
 * @author lawrence.daniels@gmail.com
 */
public class NameOp extends AbstactFileOpCode {
  private Value targetPath;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public NameOp( TokenIterator it ) throws JBasicException {
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
	  
	  // determine the full path of the source file
	  final File sourceFile = getFileReference( sourcePath, null, program, environment );
	  
	  // determine the full path of the target file
	  final File targetFile = getFileReference( targetPath, null, program, environment );
	  
	  // get the storage device
	  final IbmPcStorageSystem storage = environment.getStorageSystem();
	  
	  // rename the file
	  storage.renameFile( sourceFile.getAbsolutePath(), targetFile.getAbsolutePath() );
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
    
    // mandate the keyword "AS"
    JBasicTokenUtil.mandateToken( it, "AS" );

    // get the target file 
    targetPath = GwBasicValues.getValue( it );
    
    // no more tokens
    JBasicTokenUtil.noMoreTokens( it );
  }
  
}
