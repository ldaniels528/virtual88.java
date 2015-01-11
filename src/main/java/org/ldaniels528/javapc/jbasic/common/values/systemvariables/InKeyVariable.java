package org.ldaniels528.javapc.jbasic.common.values.systemvariables;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import java.awt.event.KeyEvent;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.ReadOnlyVariableException;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

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
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( final JBasicCompiledCodeReference program ) {
		final MemoryObject lastValue = value.duplicate();
		value.setContent( "" );
		return lastValue;
    }
    
    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Variable#setValue(org.ldaniels528.javapc.jbasic.values.types.JBasicObject)
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
