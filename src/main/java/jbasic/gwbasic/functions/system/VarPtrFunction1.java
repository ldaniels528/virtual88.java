package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.Value;
import jbasic.common.values.types.impl.JBasicTempNumber;
import jbasic.gwbasic.values.GwBasicValues;

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
     * @throws jbasic.common.exceptions.TypeMismatchException
     */
    public VarPtrFunction1( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_VAR );
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
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
     * @see jbasic.gwbasic.values.functions.GwBasicInternalFunction#lookupValue(jbasic.tokenizer.TokenIterator)
     */
    protected Value parseValues( TokenIterator it ) 
    throws JBasicException {
    		return GwBasicValues.getVariableReference( it );
    }
    
}
