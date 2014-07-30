package jbasic.gwbasic.program.commands.io;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * POKE Command
 * <br>Purpose: To write (poke) a byte of data into a memory location.
 * <br>Syntax: POKE <i>offset</i>, <i>value</i>
 * @see jbasic.gwbasic.functions.system.PeekFunction
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
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
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
