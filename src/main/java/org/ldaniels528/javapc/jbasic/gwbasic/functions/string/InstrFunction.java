package org.ldaniels528.javapc.jbasic.gwbasic.functions.string;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

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
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
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
