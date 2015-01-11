package org.ldaniels528.javapc.jbasic.gwbasic.functions.string;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * LEFT$(var$,length) Function
 */
public class LeftFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public LeftFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, new int[] { TYPE_STRING, TYPE_NUMERIC } );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
    		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the 1st parameter
		final String str = objects[0].toString();
      
		// get the 2nd parameter
		final int length = objects[1].toInteger();
		if( length < 1 ) {
			final StringMemoryObject string = new JBasicTempString();
			return string;
		}
      
		// get the substring
		final String subStr = length < str.length() 
								? str.substring( 0, length )
								: str;
      
		// return the substring
		final StringMemoryObject string = new JBasicTempString();
		string.setString( subStr );    		
		return string;
    }        

}
