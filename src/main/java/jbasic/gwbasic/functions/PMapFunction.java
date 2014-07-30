package jbasic.gwbasic.functions;

import ibmpc.devices.memory.MemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * PMAP Function (Graphics)
 * <br>Purpose: To map expressions to logical or physical coordinates.
 * <br>Syntax: x=PMAP (exp,function)
 * @author lawrence.daniels@gmail.com
 */
public class PMapFunction extends JBasicFunction {
	private static int[] PARAM_TYPES = {
		TYPE_NUMERIC, TYPE_NUMERIC
	};

    /**
     * Creates an instance of this function
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public PMapFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, PARAM_TYPES );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// identity the params we need
		final int exp = objects[0].toInteger();
		final int function = objects[1].toInteger();
		
		// get the mapped coordinate
		final int coordinate = getMappedCoordinate( exp, function );		
      
    	  	// return the position of the cursor
    	  	return new JBasicTempNumber( coordinate );
    }
    
    /**
     * Get the mapped coordinate
     * @param exp the numeric variable or expression.
     * @param function the mapping function
     * @return
     */
    private int getMappedCoordinate( int exp, int function ) {
    		final int LOGICAL_TO_PHYSICAL_X = 0;
    		final int LOGICAL_TO_PHYSICAL_Y = 1;
    		final int PHYSICAL_TO_LOGICAL_X = 2;
    		final int PHYSICAL_TO_LOGICAL_Y = 3;
    		
    		// TODO Figure out how to maps these
    		final int coordinate;
    		switch( function ) {
    			case LOGICAL_TO_PHYSICAL_X: coordinate = exp; break;
    			case LOGICAL_TO_PHYSICAL_Y: coordinate = exp; break;
    			case PHYSICAL_TO_LOGICAL_X: coordinate = exp; break;
    			case PHYSICAL_TO_LOGICAL_Y: coordinate = exp; break;
    			default: coordinate = exp;
    		}		
    		return coordinate;
    }

}
