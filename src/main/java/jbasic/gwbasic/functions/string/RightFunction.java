package jbasic.gwbasic.functions.string;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempString;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * RIGHT$(var$,length) Function
 */
public class RightFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public RightFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, new int[] { TYPE_STRING, TYPE_NUMERIC } );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    	  	// get the source string
		final String str = objects[0].toString();
      
		// get the length
		final int length = objects[1].toInteger();
      
		// extract the substring
		final String subStr = ( str.length() >= length ) 
								? str.substring( str.length() - length, str.length() )
								: str;
      
		// return the substring
		final StringMemoryObject string = new JBasicTempString();
		string.setString( subStr );    		
		return string;
    }
    
 }