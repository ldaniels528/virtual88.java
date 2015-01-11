package org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * VAL(x$) Function
 * <br>Description: Returns the numerical value of string x$.
 * @author lawrence.daniels@gmail.com
 */
public class ValFunction extends JBasicFunction {
	  
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException
     */
    public ValFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_STRING );
    }

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    		// get the source value
    		final String sval = objects[0].toString();
    		
    		// return the string
    		final NumberMemoryObject decimal = new JBasicTempNumber( GwBasicValues.parseDecimalString( sval ) );
    		return decimal;
    }
    
}