package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * Fix Function
 * <br>Purpose: To truncate x to a whole number
 * @author lawrence.daniels@gmail.com
 */
public class FixFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public FixFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    		// get the number as an integer
    		final int number = objects[0].toInteger();
    		
    		// return the integer equivalent
    		final NumberMemoryObject integer = new JBasicTempNumber( number );
    		return integer;
    }

}
