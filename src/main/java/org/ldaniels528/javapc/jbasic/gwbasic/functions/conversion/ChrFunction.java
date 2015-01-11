package org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * CHR$(x) Function
 * <br>Purpose: To generate ASCII Code characters
 * <br>Syntax: CHR$(x)
 * <br>Example: PRINT CHR$(127)
 */
public class ChrFunction extends JBasicFunction {

    /**
     * Creates an instance of this {@link org.ldaniels528.javapc.jbasic.common.values.Function function}
     * @param it the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    public ChrFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the number
		final int asciiCode = objects[0].toInteger();
		
	  	// if the code is not between 0 and 255, error ...
	  	if( asciiCode < 0 || asciiCode > 255 ) 
	  		throw new IllegalFunctionCallException();
	  
	  	// return the ascii code
		final StringMemoryObject string = new JBasicTempString();
		string.setString( new String( new byte[] { (byte)asciiCode } ) );
		return string;		
    }

}
