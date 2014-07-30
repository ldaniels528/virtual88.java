package jbasic.gwbasic.functions.conversion;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * ORD(var$) Function
 */
public class OrdFunction extends JBasicFunction {

    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator iterator}
     * @throws TypeMismatchException
     */
    public OrdFunction( String name, TokenIterator it ) throws JBasicException {
      super( name, it, TYPE_STRING );
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
      
		// get the ordinal value
		final int ordNum = ( str != null && str.length() > 0 ) ? str.charAt( 0 ) : 0;
      
		// create an instance to return
		final NumberMemoryObject integer = new JBasicTempNumber( ordNum );
		return integer;
    }

}
