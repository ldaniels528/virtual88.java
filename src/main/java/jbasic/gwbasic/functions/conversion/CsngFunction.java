package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * CSNG Function
 * <br>Purpose: To convert x to a single-precision number.
 * <br>Syntax: CSNG(x)
 * @author lawrence.daniels@gmail.com
 */
public class CsngFunction extends JBasicFunction {
	  
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public CsngFunction( String name, TokenIterator it ) 
    throws JBasicException {
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
    		final float number = (float)objects[0].toDoublePrecision();
    		
    		// return the decimal
    		final NumberMemoryObject decimal = new JBasicTempNumber( number );
    		return decimal;
    }

}
