package jbasic.gwbasic.program.commands.io.file;

import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.system.IbmPcSystem;

import java.io.File;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

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
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
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
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
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
