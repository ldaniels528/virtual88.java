package jbasic.common.values.systemvariables;

import ibmpc.devices.memory.MemoryObject;

import java.awt.event.KeyEvent;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.ReadOnlyVariableException;
import jbasic.common.values.types.impl.JBasicTempString;

/**
 * InKey Function
 */
public class InKeyVariable extends JBasicSystemVariable {

	/**
	 * Default Constructor 
	 */
    public InKeyVariable() {
      super( "INKEY$", new JBasicTempString() );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( final JBasicCompiledCodeReference program ) {
		final MemoryObject lastValue = value.duplicate();
		value.setContent( "" );
		return lastValue;
    }
    
    /* 
     * (non-Javadoc)
     * @see jbasic.values.Variable#setValue(jbasic.values.types.JBasicObject)
     */
    public void setValue( final MemoryObject value ) {
    	throw new ReadOnlyVariableException();
    }
    
    /**
     * Updates this variable with the last keystroke from the keyboard
     * @param event the event created by the last keystroke
     */
    public void update( KeyEvent event ) {
    	final String keyStroke = String.valueOf( event.getKeyChar() );
    	value.setContent( keyStroke );
    }

}
