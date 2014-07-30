package jbasic.gwbasic.program.commands.io.device;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.exceptions.IbmPcException;

import java.util.ArrayList;
import java.util.List;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.UnexpectedTokenException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * PRINT# and PRINT# USING Statements
 * <br>Purpose: To write data to a sequential disk file.
 * <br>Syntax: PRINT#file number,[USING <i>string expressions</i>;] <i>list of expressions</i>
 * @author lawrence.daniels@gmail.com
 */
public class PrintDevOp extends GwBasicCommand {
	  private final List<Value> values;
	  private final int fileHandle;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public PrintDevOp( TokenIterator it ) throws JBasicException {
		  // construct the values container
		  this.values = new ArrayList<Value>();
	    
		  // expect "#"
		  JBasicTokenUtil.mandateToken( it,"#" );
		  
		  // get the file handle first
		  this.fileHandle = (int)JBasicTokenUtil.nextNumericToken( it );
		  
		  // parse the elements that will be printed
		  parse( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // construct a list of object
		  List<MemoryObject> objects = new ArrayList<MemoryObject>( values.size() );
		  for( Value value : values ) {
			  objects.add( value.getValue( program ) );
		  }

		  // get the storage device
		  final IbmPcStorageSystem storage = program.getSystem().getStorageSystem();
		  
		  // write objects to the device
		  try {
			storage.writeToDevice( fileHandle, objects );
			// TODO need CRLF?
		  } 
		  catch( final IbmPcException e ) {
			throw new JBasicException( e );
		  }
	  }

	  /**
	   * Converts the given textual representation into {@link Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link TokenIterator iterator}
	   * @throws JBasicException
	   */
	  private void parse( TokenIterator it ) throws JBasicException {
		  while( it.hasNext() ) {	    		    	
		      // add the next value
		      values.add( GwBasicValues.getValueReference( it ) );
	
		      // if there are more tokens, it should be a semicolon
		      if( it.hasNext() ) {
		        if( it.peekAtNext().equals( "," ) || it.peekAtNext().equals( ";" ) )
		          it.next();
		        else
		          throw new UnexpectedTokenException( ",", it.next() );        
		      }
		  }
	  }

}
