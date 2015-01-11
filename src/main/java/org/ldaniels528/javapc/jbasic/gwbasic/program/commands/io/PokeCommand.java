package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * POKE Command
 * <br>Purpose: To write (poke) a byte of data into a memory location.
 * <br>Syntax: POKE <i>offset</i>, <i>value</i>
 * @see org.ldaniels528.javapc.jbasic.gwbasic.functions.system.PeekFunction
 * @author lawrence.daniels@gmail.com
 */
public class PokeCommand extends GwBasicCommand {
	private final Value offsetVal;
	private final Value dataVal;

  /**
   * Default Constructor
   * @param it the given {@link TokenIterator token iterator}
   * @throws JBasicException
   */
  public PokeCommand( TokenIterator it ) throws JBasicException {
	  final Value[] params = JBasicTokenUtil.parseValues( it, 2, 2 );
	  this.offsetVal = params[0];
	  this.dataVal	= params[1];
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the offset
	  MemoryObject theOffset = offsetVal.getValue( program );
	  if( !theOffset.isNumeric() )
		  throw new TypeMismatchException( theOffset );
	  	  
	  // get the data
	  MemoryObject theData = dataVal.getValue( program );
	  if( !theData.isNumeric() )
		  throw new TypeMismatchException( theData );
	  
	  // poke the data into memory	 
	  final int segment = program.getDefaultMemorySegment();
	  final int offset  = GwBasicValues.getUnsignedInt( theOffset.toInteger() );
	  final byte data   = (byte)theData.toInteger();	  
	  final IbmPcRandomAccessMemory memory = program.getSystem().getRandomAccessMemory();
	  memory.setByte( segment, offset, data );	  
  }

}
