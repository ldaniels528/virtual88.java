package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

/** 
 * USR Function
 * <br>Purpose: To call an assembly language subroutine.
 * <br>Syntax: <i>variable</>=USR[n](<i>argument</i>)
 * <br>Example: X=USR0(Y^2/2.82)
 * @see jbasic.gwbasic.program.commands.system.DefUsrOp
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
	 * @see jbasic.base.values.Value#getValue(jbasic.base.environment.JBasicEnvironment)
	 */
	public MemoryObject getValue( final JBasicCompiledCodeReference compiledCodeRef ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( compiledCodeRef );
		
	  	// return the amount of free memory
	  	final NumberMemoryObject number = new JBasicTempNumber( 0 );
	  	return number;
	}

}
