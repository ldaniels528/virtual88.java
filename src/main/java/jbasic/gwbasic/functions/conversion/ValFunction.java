package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * VAL(x$) Function
 * <br>Description: Returns the numerical value of string x$.
 * @author lawrence.daniels@gmail.com
 */
public class ValFunction extends JBasicFunction {
	  
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws jbasic.common.exceptions.TypeMismatchException
     */
    public ValFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_STRING );
    }

	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
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