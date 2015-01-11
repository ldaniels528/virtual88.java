package org.ldaniels528.javapc.jbasic.gwbasic.functions.system;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

/** 
 * USR Function
 * <br>Purpose: To call an assembly language subroutine.
 * <br>Syntax: <i>variable</>=USR[n](<i>argument</i>)
 * <br>Example: X=USR0(Y^2/2.82)
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.DefUsrOp
 * @author lawrence.daniels@gmail.com
 */
public class UsrFunction extends JBasicFunction {

	/** 
	 * Creates an instance of this function
	 * @param name the given name of the function
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	public UsrFunction( String name, TokenIterator it ) 
	throws JBasicException {
		super( name, it, TYPE_ANY );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.Value#getValue(org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	 */
	public MemoryObject getValue( final JBasicCompiledCodeReference compiledCodeRef ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( compiledCodeRef );
		
	  	// return the amount of free memory
	  	final NumberMemoryObject number = new JBasicTempNumber( 0 );
	  	return number;
	}

}
