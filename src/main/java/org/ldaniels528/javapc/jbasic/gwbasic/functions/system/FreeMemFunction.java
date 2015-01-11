package org.ldaniels528.javapc.jbasic.gwbasic.functions.system;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * FRE(<i>var</i>) Function
 * <br>Purpose: This function reports the amount of free memory
 * @author lawrence.daniels@gmail.com
 */
public class FreeMemFunction extends JBasicFunction {

    /**
     * Creates an instance of this function
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public FreeMemFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_ANY );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.common.values.Value#getValue(org.ldaniels528.javapc.ibmpc.assembly.CompiledCodeReference)
     */
    public MemoryObject getValue( final JBasicCompiledCodeReference code ) {
    		// get the memory manager
    		final MemoryManager memoryManager = code.getMemoryManager();
    	
    	  	// get the amount of free memory
    	  	final int freeMemory = memoryManager.getUnallocatedAmount();
      
    	  	// return the amount of free memory
    	  	return new JBasicTempNumber( freeMemory );
    }

}