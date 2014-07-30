package jbasic.common.values.systemvariables;

import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.ReadOnlyVariableException;
import jbasic.common.values.types.impl.JBasicTempNumber;

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
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
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
     * @see jbasic.values.Variable#setValue(jbasic.values.types.JBasicObject)
     */
    public void setValue( MemoryObject value ) {
    		throw new ReadOnlyVariableException();
    }

}
