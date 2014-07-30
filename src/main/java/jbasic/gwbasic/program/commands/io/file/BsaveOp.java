package jbasic.gwbasic.program.commands.io.file;

import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.system.IbmPcSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * BSAVE Command
 * <br>Syntax: BSAVE filename,offset,length
 * @author lawrence.daniels@gmail.com
 */
public class BsaveOp extends AbstactFileOpCode {
	private Value offset;
	private Value length;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public BsaveOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
    // get the environment
	final IbmPcSystem environment = program.getSystem();
	  
	// determine the full path of the file
	final File thePath = getFileReference( sourcePath, null, program, environment );
	
	// get the default segment
	final int theSegment = program.getDefaultMemorySegment();
	
	// get the offset
	final MemoryObject theOffset = offset.getValue( program );
	if( !theOffset.isNumeric() )
		throw new TypeMismatchException( theOffset );
	
	// get the data length
	final MemoryObject theLength = length.getValue( program );
	if( !theLength.isNumeric() )
		throw new TypeMismatchException( theLength );
	
    // get a reference to random access memory
    final IbmPcRandomAccessMemory memory = environment.getRandomAccessMemory();	      
    
    // save the binary data
    saveBinaryData( memory, thePath, theSegment, 
    				   GwBasicValues.getUnsignedInt( theOffset.toInteger() ), 
    				   GwBasicValues.getUnsignedInt( theLength.toInteger() ) );
  }
  
  /**
   * Save binary data from memory to disk
   * @param file the given binary file
   */
  private void saveBinaryData( IbmPcRandomAccessMemory memory, 
		  					 File file,
		  					 int segment,
		  					 int offset, 
		  					 int length ) 
  throws JBasicException {
	  OutputStream out = null;
	  try {
		  // open the file [for write access]
		  out = new FileOutputStream( file );
		  
		  // get the block of binary data from memory
		  byte[] block = memory.getBytes( segment, offset, length );
		  
		  // write the block to disk
		  out.write( block );
	  } 
	  catch( IOException e ) {
		throw new JBasicException( e );
	  }
	  finally {
		  if( out != null ) try { out.close(); } catch( Exception e) { }
	  }
  }
  
  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  protected void parse( TokenIterator it ) throws JBasicException {
	  Value[] params = JBasicTokenUtil.parseValues( it, 3, 3 );
	  
	  // get the file reference
	  sourcePath = params[0];

	  // get the offset value    
	  offset = params[1];
    
	  // get the length value    
	  length = params[2];
  }

}
