package jbasic.common.values.systemvariables;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.OutOfMemoryException;
import ibmpc.system.IbmPcSystem;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.ReadOnlyVariableException;
import jbasic.common.values.types.impl.JBasicTempNumber;

/** 
 * CSRLIN Variable
 * <br>Purpose: To return the current line (row) position of the cursor.
 * <br>Syntax: y=CSRLIN
 * @author lawrence.daniels@gmail.com
 */
public class CursorLineVariable extends JBasicSystemVariable {

	/**
	 * Default Constructor 
	 * @throws OutOfMemoryException 
	 */
    public CursorLineVariable() {
    	super( "CSRLIN", new JBasicTempNumber( 0 ) );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the environment
		final IbmPcSystem environment = program.getSystem();
	
		// get a reference to the screen
		final IbmPcDisplay screen = environment.getDisplay();
		
		// get the current display mode
		final IbmPcDisplayMode displayMode = screen.getDisplayMode();
		
		// get the cursor position 
		final int cursorPosition = screen.getCursorPosition();
		
		// get the number of display columns
		final int columns = displayMode.getColumns();
		
		// calculate the row of the cursor position
		final int row = cursorPosition / columns;
		
		// set the value 
		value.setContent( row );
		
		// return the value
		return value;
    }
    
    /* 
     * (non-Javadoc)
     * @see jbasic.values.Variable#setValue(jbasic.values.types.JBasicObject)
     */
    public void setValue( MemoryObject value ) {
    	throw new ReadOnlyVariableException();
    }

}
