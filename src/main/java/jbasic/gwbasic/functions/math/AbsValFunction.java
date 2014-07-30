package jbasic.gwbasic.functions.math;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * ABS(x) Function
 */
public class AbsValFunction extends JBasicFunction {
	  
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public AbsValFunction( String name, TokenIterator it ) throws JBasicException {
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
    		final double number = objects[0].toDoublePrecision();
    		
    		// return the absolute value of the number
    		final NumberMemoryObject decimal = new JBasicTempNumber( number );
    		return decimal;
    }

}
