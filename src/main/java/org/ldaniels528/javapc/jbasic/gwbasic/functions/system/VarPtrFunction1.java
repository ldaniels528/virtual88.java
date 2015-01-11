package org.ldaniels528.javapc.jbasic.gwbasic.functions.system;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * VARPTR Function
 * <br>Syntax: VARPTR(<i>variable</i>)
 * <br>Purpose: To return the address in memory of the variable or 
 * file control block (FCB).
 * @author lawrence.daniels@gmail.com
 */
public class VarPtrFunction1 extends JBasicFunction {
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator token iterator}
     * @throws org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException
     */
    public VarPtrFunction1( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_VAR );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the physical address
		final int offset = objects[0].getOffset();
		
		// return the square root
		final NumberMemoryObject integer = new JBasicTempNumber( offset );
		return integer;
    }
    
    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.gwbasic.values.functions.GwBasicInternalFunction#lookupValue(org.ldaniels528.javapc.jbasic.tokenizer.TokenIterator)
     */
    protected Value parseValues( TokenIterator it ) 
    throws JBasicException {
    		return GwBasicValues.getVariableReference( it );
    }
    
}
