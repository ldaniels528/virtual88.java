package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.console;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicDisplayModes;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * WIDTH Command
 * <br>Purpose: Sets the column width
 * <br>Syntax:  WIDTH <i>columns</i>
 * <br>Example: WIDTH 80
 */
public class WidthOp extends GwBasicCommand {
  private final Value columns;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
   */
  public WidthOp( TokenIterator it ) throws JBasicException {
	    // get the value
	    columns = GwBasicValues.getValueReference( it );

	    // if any more tokens, error ...
	    JBasicTokenUtil.noMoreTokens( it );
  }

  /**
   * Executes this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	    // validate the value type
	    MemoryObject object = columns.getValue( program );	
	    if( !object.isNumeric() )
	      throw new TypeMismatchException( object );
	    
	    // get the screen instance
	    final IbmPcDisplay screen = program.getSystem().getDisplay();
	
	    // lookup the current mode
	    final IbmPcDisplayMode mode = screen.getDisplayMode();
	    
	    // get the column width
	    final int columns = object.toInteger();
	    
	    // set the column width
	    final IbmPcDisplayMode newMode = changeWidth( mode, columns );
	    if( newMode != null )
	    		screen.setDisplayMode( newMode );
	    else
	    		throw new IllegalFunctionCallException( this );
  }
  
	/**
	 * Returns the display mode that corresponds to the given
	 * column width criteria.
	 * @param columnWidth the given column width
	 * @return the matching {@link IbmPcDisplayMode display mode}
	 */
	public IbmPcDisplayMode changeWidth( IbmPcDisplayMode mode, int columnWidth ) {
		switch( columnWidth ) {
			case 40: return mode.isGraphical() ? JBasicDisplayModes.MODE1 : JBasicDisplayModes.MODE0a;					 
			case 80: return mode.isGraphical() ? JBasicDisplayModes.MODE2 : JBasicDisplayModes.MODE0b;
		}
		return null;
	}

}
