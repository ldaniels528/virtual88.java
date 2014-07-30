package jbasic.gwbasic.program.commands.console;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;

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
   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
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
