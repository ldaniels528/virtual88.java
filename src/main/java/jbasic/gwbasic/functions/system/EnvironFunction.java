package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempString;

import com.ldaniels528.tokenizer.TokenIterator;

/** 
 * ENVIRON$ Function
 * <br>Purpose: To allow the user to retrieve the specified environment 
 * string from the environment table.
 * <br>Syntax:
 * <br>v$=ENVIRON$(parmid)
 * <br>v$=ENVIRON$(nthparm)
 * @author lawrence.daniels@gmail.com
 */
public class EnvironFunction extends JBasicFunction {

	/** 
	 * Creates an instance of this function 
	 * @param name the name of this function
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	protected EnvironFunction( String name, TokenIterator it ) throws JBasicException {
		super( name, it, TYPE_STRING );
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.base.values.Value#getValue(jbasic.base.environment.JBasicEnvironment)
	 */
	public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the environment string
		final String str = objects[0].toString();
		
		// return the appropriate environment value		
		StringMemoryObject string = new JBasicTempString(); 
		string.setString( str ); // TODO Figure out how to get/set environment
		return string;
	}

}
