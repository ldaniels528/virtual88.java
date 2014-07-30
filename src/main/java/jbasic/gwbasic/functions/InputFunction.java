/**
 * 
 */
package jbasic.gwbasic.functions;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempString;

/**
 * INPUT$(n) Function
 * <br>Syntax: INPUT$(<i>n</i>)
 * <br>Example: str$ = INPUT$(1)
 * @author lawrence.daniels@gmail.com
 */
public class InputFunction extends JBasicFunction {

	/**
	 * Creates an instance of this function
	 * @param name the name of this function
	 * @param it the given {@link TokenIterator token iterator}
	 * @param paramType the given parameter type
	 * @throws JBasicException
	 */
	protected InputFunction( String name, TokenIterator it ) 
	throws JBasicException {
		super( name, it, TYPE_NUMERIC );
	}

	/* (non-Javadoc)
	 * @see ibmpc.program.values.Value#getValue(ibmpc.program.IbmPcProgram)
	 */
	public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the number of characters
		final int nchars = objects[0].toInteger(); 
		
		// get the keyboard instance
		final IbmPcKeyboard keyboard = program.getSystem().getKeyboard();
		
		// read the given number of characters from the keyboard
		final String data = keyboard.next( nchars );
		
		// return the value
		return new JBasicTempString( data );
	}

}
