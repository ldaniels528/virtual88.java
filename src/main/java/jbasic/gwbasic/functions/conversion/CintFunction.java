package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.OverflowException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * CINT(x) Function
 * <br>Purpose: To round numbers with fractional portions to the next whole number or integer.
 * @author lawrence.daniels@gmail.com
 */
public class CintFunction extends JBasicFunction {
	  
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public CintFunction( String name, TokenIterator it ) throws JBasicException {
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
    		
    		// check for overflow
    		if( number < -32768 || number > 32767 )
    			throw new OverflowException( number );
    		
    		// return the rounded integer
    		final NumberMemoryObject integer = new JBasicTempNumber( (int)Math.round( number ) );
    		return integer;
    }

}