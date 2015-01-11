package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/** 
 * EOF Function
 * <pre>
 * Purpose: To return -1 (true) when the end of a sequential or 
 * a communications file has been reached, or to return 0 if 
 * end of file (EOF) has not been found.
 * Syntax: v=EOF(file number)
 * <pre>
 * @author lawrence.daniels@gmail.com
 */
public class EofFunction extends JBasicFunction {

    /**
     * Creates an instance of this function
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public EofFunction( String name, TokenIterator it ) 
    throws JBasicException {
    		super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( final JBasicCompiledCodeReference compiledCodeRef ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( compiledCodeRef );
		
		// get the environment string
		final int fileNumber = objects[0].toInteger();
      
		// lookup the file handle
		// TODO Add the ability to read EOF Status from a file handle
		
	  	// return the amount of free memory
	  	final NumberMemoryObject number = new JBasicTempNumber( 0 );
	  	return number;
    }

}
