package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.MemoryManager;
import ibmpc.devices.memory.MemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

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
     * @see jbasic.common.values.Value#getValue(ibmpc.assembly.CompiledCodeReference)
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