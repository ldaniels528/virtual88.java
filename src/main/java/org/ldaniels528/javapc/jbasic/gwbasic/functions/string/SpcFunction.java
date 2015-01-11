package org.ldaniels528.javapc.jbasic.gwbasic.functions.string;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * SPC(x) Function
 */
public class SpcFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator token iterator}
     * @throws TypeMismatchException
     */
    public SpcFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    	  	// get the input parameter
		final int count = objects[0].toInteger();
      
		// build the space filled string
		final StringBuilder sb = new StringBuilder();
		for( int i = 0; i < count; i++ ) {
			sb.append( ' ' );
		}
      
		// return the string
		final StringMemoryObject string = new JBasicTempString();
		string.setString( sb.toString() );    		
		return string;
    }
    
 }