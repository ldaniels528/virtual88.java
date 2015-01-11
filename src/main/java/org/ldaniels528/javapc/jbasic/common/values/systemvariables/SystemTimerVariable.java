package org.ldaniels528.javapc.jbasic.common.values.systemvariables;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.ReadOnlyVariableException;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * Timer Function
 */
public class SystemTimerVariable extends JBasicSystemVariable {

	/**
	 * Default constructor
	 */
    public SystemTimerVariable() {
      super( "TIMER", new JBasicTempNumber( 0.0d ) );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
    		// get the system time
    		final long systemTime = System.currentTimeMillis();
    		
    		// set the content
    		value.setContent( systemTime );
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
