package jbasic.gwbasic.program.commands.io.file;

import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.system.IbmPcSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.FileNotFoundException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * BLOAD Command
 * <br>Syntax: BLOAD filename[,offset]
 * @author lawrence.daniels@gmail.com
 */
public class BloadOp extends AbstactFileOpCode {
	private Value offset;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public BloadOp( TokenIterator it ) throws JBasicException {
    super( it );
  }

  /**
   * Executes this {@link jbasic.common.program.OpCode opCode}
 * @throws JBasicException
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the environment
	  final IbmPcSystem environment = program.getSystem();
	  
	  // determine the full path of the file
	  final File theFile = getFileReference( sourcePath, null, program, environment );
    
	  // file must be found
	  if( !theFile.exists() )
		  throw new FileNotFoundException( theFile );
    
	  // get the offset
	  int theOffset = 0;
	  if( offset != null ) {
		  MemoryObject value = offset.getValue( program );
    			if( !value.isNumeric() )
    				throw new TypeMismatchException( value );
    			theOffset = value.toInteger();
	  }
    
	  // get the default segment
	  final int theSegment = program.getDefaultMemorySegment();
    
	  // get a reference to random access memory
	  final IbmPcRandomAccessMemory memory = environment.getRandomAccessMemory();
    
	  // load the binary data
	  loadBinaryData( memory, theFile, theSegment, theOffset );
  }
  
  /**
   * Loads binary data from disk into memory
   * @param file the given binary file
   */
  private void loadBinaryData( IbmPcRandomAccessMemory memory, 
		  					 File file,
		  					 int segment,
		  					 int offset ) 
  throws JBasicException {
	  InputStream in = null;
	  try {
		  // open the file [for write access]
		  in = new FileInputStream( file );		 
		  
		  // read the block from the disk
		  byte[] block = loadBinaryData( in );		  
		  
		  // get the block of binary data from memory
		  memory.setBytes( segment, offset, block, block.length );
	  } 
	  catch( IOException e ) {
		throw new JBasicException( e );
	  }
	  finally {
		  if( in != null ) try { in.close(); } catch( Exception e) { }
	  }
  }
  
  /**
   * Loads the binary data from the given stream
   * @param in the given {@link InputStream stream}
   * @return the binary data from the given stream
   * @throws IOException
   */
  private byte[] loadBinaryData( InputStream in ) 
  throws IOException {
	  // create a memory stream
	  ByteArrayOutputStream baos = new ByteArrayOutputStream( 1024 );
	  
	  // create a memory buffer 
	  byte[] buffer = new byte[1024];
	  
	  // write the contents of the stream to the memory stream
	  int count = 0;
	  while( ( count = in.read( buffer ) ) != -1 ) {
		  baos.write( buffer, 0, count );
	  }
	  
	  // return the binary data
	  return baos.toByteArray();
  }
  
  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  protected void parse( TokenIterator it ) throws JBasicException {
    // get the file reference
    sourcePath = GwBasicValues.getValue( it );

    // are there other parameters?
    if( it.hasNext() ) {
	    // get the offset value
    		JBasicTokenUtil.mandateToken( it, "," );
	    offset = GwBasicValues.getValue( it );
	    
	    // there should be no more elements
	    JBasicTokenUtil.noMoreTokens( it );
    }
  }

}