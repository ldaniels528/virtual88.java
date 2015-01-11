package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

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
	 * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
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
