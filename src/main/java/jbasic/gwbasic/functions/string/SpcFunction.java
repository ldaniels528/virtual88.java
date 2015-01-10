package jbasic.gwbasic.functions.string;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempString;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * SPC(x) Function
 */
public class SpcFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator token iterator}
     * @throws TypeMismatchException
     */
    public SpcFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    	  	// get the input parameter
		final int count = objects[0].toInteger();
      
		// build the space filled string
		final StringBuilder sb = new StringBuilder();
		for( int i = 0; i < count; i++ ) {
			sb.append( ' ' );
		}
      
		// return the string
		final StringMemoryObject string = new JBasicTempString();
		string.setString( sb.toString() );    		
		return string;
    }
    
 }