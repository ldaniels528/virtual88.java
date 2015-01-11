package jbasic.gwbasic.functions.math;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * ATN(x) Function
 */
public class ArcTanFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public ArcTanFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_NUMERIC );
    }  
	  
	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
	 */
	public MemoryObject getValue(JBasicCompiledCodeReference program) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    		// get the angle
    		final double angle = objects[0].toDoublePrecision();
		
		// return the new typed value
		final NumberMemoryObject decimal = new JBasicTempNumber( Math.atan( angle ) );
		return decimal;
	}

}
