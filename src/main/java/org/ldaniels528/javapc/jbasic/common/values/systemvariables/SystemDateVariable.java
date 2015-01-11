package org.ldaniels528.javapc.jbasic.common.values.systemvariables;


import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import java.text.SimpleDateFormat;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.ReadOnlyVariableException;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

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
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
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
     * @see org.ldaniels528.javapc.jbasic.values.Variable#setValue(org.ldaniels528.javapc.jbasic.values.types.JBasicObject)
     */
    public void setValue( MemoryObject value ) {
    		throw new ReadOnlyVariableException();
    }

}
