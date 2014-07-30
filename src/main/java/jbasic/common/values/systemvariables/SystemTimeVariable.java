package jbasic.common.values.systemvariables;

import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.ReadOnlyVariableException;
import jbasic.common.values.types.impl.JBasicTempString;

/**
 * Time System Variable
 */
public class SystemTimeVariable extends JBasicSystemVariable {

	/**
	 * Default constructor  
	 */
    public SystemTimeVariable() {
      super( "TIME$", new JBasicTempString() );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
    		// get the system time
    		final String systemTime = new java.sql.Time( System.currentTimeMillis() ).toString();
    		
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