package jbasic.gwbasic.program.commands.io.device;

import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.exceptions.IbmPcException;

import java.util.LinkedList;
import java.util.List;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * CLOSE Command
 * <br>Syntax: CLOSE [[#]filenumber[,[#]filenumber]...]
 * <br>Example: CLOSE #1
 * @author lawrence.daniels@gmail.com
 */
public class CloseOp extends GwBasicCommand {
	  private final Integer[] handles;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public CloseOp( TokenIterator it ) throws JBasicException {
		  // initialize the container for all the file handles
		  this.handles = parse( it );		  
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  try {
			  // get the storage device
			  final IbmPcStorageSystem storage = program.getSystem().getStorageSystem();
			  
			  // if no handles specified, close all handles
			  if( handles.length == 0 ) {
				  storage.closeAllDevices();
			  }
			  // close specific handles
			  else {
				  for( final int handle : handles ) {
					  storage.closeDevice( handle );
				  }
			  }
		  }
		  catch( final IbmPcException e ) {
			  throw new JBasicException( e );
		  }
	  }
	  	  
	  /**
	   * Converts the given textual representation into {@link jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link TokenIterator iterator}
	   * @throws JBasicException
	   */
	  private Integer[] parse( TokenIterator it ) throws JBasicException {
		  final List<Integer> handles = new LinkedList<Integer>();
		  
		  // if at least one token is specified ...
		  while( it.hasNext() ) {
			  // get the file handle
			  final TokenIterator args = JBasicTokenUtil.extractTokens( it, "#", "@@" );
			  final int fileHandle = (int)JBasicTokenUtil.nextNumericToken( args );
			  
			  // add the handle to the list
			  handles.add( fileHandle );
			  
			  // if there are anymore tokens ...
			  if( it.hasNext() ) {
				  JBasicTokenUtil.mandateToken( it, "," );
			  }
		  }
		  
		  return handles.toArray( new Integer[ handles.size() ] );
	  }

}