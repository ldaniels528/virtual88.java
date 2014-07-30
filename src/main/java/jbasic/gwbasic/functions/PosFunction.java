package jbasic.gwbasic.functions;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;

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
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
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
