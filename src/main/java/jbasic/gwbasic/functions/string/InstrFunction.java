package jbasic.gwbasic.functions.string;

import ibmpc.devices.memory.MemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * INSTR Function
 * @author lawrence.daniels@gmail.com
 */
public class InstrFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public InstrFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, new int[] { TYPE_STRING, TYPE_STRING } );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    	  	// get the source and target string
		final String source = objects[0].toString();
		final String target = objects[1].toString();
      
		// create & return the position of the target string within the source
		return new JBasicTempNumber( source.indexOf( target ) );
    }

}
