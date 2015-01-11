package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempString;
import jbasic.gwbasic.values.GwBasicValues;

import jbasic.common.tokenizer.TokenIterator;

/**
 * OCT$ Function
 * <br>Purpose: To return a string which represents the hexadecimal 
 * value of the numeric argument.
 * <br>Syntax: v$=OCT$(x)
 * @author lawrence.daniels@gmail.com
 */
public class OctFunction extends JBasicFunction {

    /**
     * Creates an instance of this {@link jbasic.common.values.Function function}
     * @param it the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    public OctFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the number
		final String octNum = GwBasicValues.toOctal( objects[0].toInteger() );
    		
	  	// return the ascii code
		final StringMemoryObject string = new JBasicTempString();
		string.setString( octNum );    		
		return string;
    }

}
