package org.ldaniels528.javapc.jbasic.common.values.systemvariables;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.OutOfMemoryException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.ReadOnlyVariableException;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

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
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
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
     * @see org.ldaniels528.javapc.jbasic.values.Variable#setValue(org.ldaniels528.javapc.jbasic.values.types.JBasicObject)
     */
    public void setValue( MemoryObject value ) {
    	throw new ReadOnlyVariableException();
    }

}
