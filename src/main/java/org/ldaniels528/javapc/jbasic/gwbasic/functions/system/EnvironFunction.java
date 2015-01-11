package org.ldaniels528.javapc.jbasic.gwbasic.functions.system;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

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
	 * @see org.ldaniels528.javapc.jbasic.base.values.Value#getValue(org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
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
