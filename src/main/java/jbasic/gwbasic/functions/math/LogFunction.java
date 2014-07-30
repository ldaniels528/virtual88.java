package jbasic.gwbasic.functions.math;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/** 
 * LOG Function
 * <br>Purpose: To return the natural logarithm of x.
 * <br>Syntax: LOG(x)
 * @author lawrence.daniels@gmail.com
 */
public class LogFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public LogFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    		// get the value
    		final double value = objects[0].toDoublePrecision();
    		
    		// return the new typed value
    		final NumberMemoryObject decimal = new JBasicTempNumber( Math.log( value ) );
    		return decimal;
    }

}
