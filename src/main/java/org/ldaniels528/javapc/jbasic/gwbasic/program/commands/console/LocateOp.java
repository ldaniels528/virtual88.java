package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.console;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * Change Cursor Location
 * Syntax: LOCATE <i>row</i>, <i>col</i>
 */
public class LocateOp extends GwBasicCommand {
  private final Value columnVal;
  private final Value rowVal;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public LocateOp( TokenIterator it ) throws JBasicException {
	  // parse all the values
	  final Value[] values = JBasicTokenUtil.parseValues( it, 2, 2 );
	  
	  // identify our values for use
	  this.rowVal 	= values[0];
	  this.columnVal = values[1];      
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the row and column
	  final MemoryObject col = columnVal.getValue( program );
	  final MemoryObject row = rowVal.getValue( program );
	  
	  // get the display instance
	  final IbmPcDisplay screen = program.getSystem().getDisplay();
	  
	  // change the cursor position
	  screen.setCursorPosition( col.toInteger(), row.toInteger() );
  }

}
