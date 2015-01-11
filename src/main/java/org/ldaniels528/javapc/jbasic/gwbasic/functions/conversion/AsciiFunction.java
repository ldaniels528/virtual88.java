package org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * ASC(x) Function
 * <br>Purpose: To return a numeric value that is the ASCII code 
 * for the first character of the string x$.
 * <br>Syntax: ASC(<i>x$</i>)
 */
public class AsciiFunction extends JBasicFunction {
	  
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public AsciiFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_STRING );
    }

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the string
		final String string = objects[0].toString();
		if( string.length() == 0 )
			throw new IllegalFunctionCallException();
		
    		// get the first character
    		final char stringChar = string.charAt( 0 );
    		
    		// return the ordinal representation of the character
    		final NumberMemoryObject integer = new JBasicTempNumber( stringChar );    		
    		return integer;    	
    }
    
}
