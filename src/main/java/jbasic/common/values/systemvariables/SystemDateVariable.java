package jbasic.common.values.systemvariables;


import ibmpc.devices.memory.MemoryObject;

import java.text.SimpleDateFormat;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.ReadOnlyVariableException;
import jbasic.common.values.types.impl.JBasicTempString;

/**
 * System Date Function
 */
public class SystemDateVariable extends JBasicSystemVariable {
	private SimpleDateFormat dateFormat = new SimpleDateFormat( "MM-dd-yyyy" );

	/**
	 * Default Constructor 
	 */
    public SystemDateVariable() {
    		super( "DATE$", new JBasicTempString() );
    }        

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
    		// get the system date
    		final java.sql.Date now = new java.sql.Date( System.currentTimeMillis() );
    		final String systemDate = dateFormat.format( now );
    		
    		// set the content
    		value.setContent( systemDate );
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
