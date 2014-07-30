package jbasic.gwbasic.functions.math;


import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import java.util.Random;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * Random Number Generator
 * RND(<i>number</i>) Function
 */
public class RndFunction extends JBasicFunction {
	private static final int MIN_PARAMS	 = 0;
	private static final int[] PARAM_TYPES = {
		TYPE_NUMERIC
	};

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws TypeMismatchException
     */
    public RndFunction( String name, TokenIterator it ) 
    throws JBasicException {
      super( name, it, PARAM_TYPES, MIN_PARAMS );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
  		// get the random seed		
		final long seed = ( objects.length > 0 ) ? objects[0].toInteger() : System.currentTimeMillis();
	
		// get the randomized value
		final double randomValue = new Random( seed ).nextDouble();
      
		// return the randomized value
		final NumberMemoryObject decimal = new JBasicTempNumber( randomValue );
		return decimal;
    }
    
 }