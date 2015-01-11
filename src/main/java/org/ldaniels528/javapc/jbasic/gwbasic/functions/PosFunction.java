package org.ldaniels528.javapc.jbasic.gwbasic.functions;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempNumber;

/**
 * POS Function
 * <br>Purpose: To return the current cursor position.
 * <br>Syntax: POS(c)
 * @author lawrence.daniels@gmail.com
 */
public class PosFunction extends JBasicFunction {

    /**
     * Creates an instance of this function
     * @param it the given {@link TokenIterator iterator}
     * @throws JBasicException
     */
    public PosFunction( String name, TokenIterator it ) throws JBasicException {
    		super( name, it, TYPE_NUMERIC );
    }

    /* 
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.values.Value#getValue(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
    		// get a screen instance
    		final IbmPcDisplay screen = program.getSystem().getDisplay();
    		
    	  	// get the position of the cursor
    	  	final int position = screen.getCursorPosition();
      
    	  	// return the position of the cursor
    	  	return new JBasicTempNumber( position );
    }

}
