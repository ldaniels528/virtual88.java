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
 * TAB(x) Function
 */
public class TabFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator token iterator}
     * @throws TypeMismatchException
     */
    public TabFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
    	  	// get the count
		final int count = objects[0].toInteger();
      
		// build the string
		final StringBuffer sb = new StringBuffer( count );
		for( int n = 0; n < count; n++ ) {
			sb.append( '\t' );
		}
      
		// return the string
		final StringMemoryObject string = new JBasicTempString();
		string.setString( sb.toString() );    		
		return string;
    }
    
  }