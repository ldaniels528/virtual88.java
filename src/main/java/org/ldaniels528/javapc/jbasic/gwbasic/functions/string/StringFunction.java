package org.ldaniels528.javapc.jbasic.gwbasic.functions.string;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * STRING$(<i>var$</i>,<i>start</i>,<i>length</i>) Function
 */
public class StringFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    public StringFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, new int[] { TYPE_STRING, TYPE_NUMERIC, TYPE_NUMERIC } );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    	  	// get the source string
		final String str = objects[0].toString();
      
		// get the starting index
		final int start = objects[1].toInteger();
      
		// get the length for the substring
		final int length = objects[2].toInteger();
      
		// extract the substring
		final String subStr = str.substring( start, start + length );
      
		// return the string
		final StringMemoryObject string = new JBasicTempString();
		string.setString( subStr );    		
		return string;
    }
    
  }