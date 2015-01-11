package org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * HEX$ Function
 * <br>Purpose: To return a string which represents the hexadecimal 
 * value of the numeric argument.
 * <br>Syntax: v$=HEX$(x)
 * @author lawrence.daniels@gmail.com
 */
public class HexFunction extends JBasicFunction {

    /**
     * Creates an instance of this {@link org.ldaniels528.javapc.jbasic.common.values.Function function}
     * @param it the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    public HexFunction( String name, TokenIterator it ) throws JBasicException {
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
    	final String hexNum = GwBasicValues.toHexadecimal( objects[0].toInteger() );
    		
	  	// return the ascii code
		final StringMemoryObject string = new JBasicTempString();
		string.setString( hexNum );    		
		return string;
    }

}
