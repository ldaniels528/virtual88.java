package jbasic.common.program;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.values.Variable;

/**
 * Represents a Conditional Control Block for performing loops
 * @author lawrence.daniels@gmail.com
 */
public class ConditionalControlBlock {	
    private final ConditionalOpCode opCode;
    private final Variable variable;
    private final int opCodePointer;

    /**
     * Creates a instance of this conditional control
     * @param variable the given {@link Variable variable}
     * @param opCode the given {@link ConditionalOpCode conditional opCode}
     * @param opCodePointer the positional offset within the code
     */
    public ConditionalControlBlock( final Variable variable, 
    								final ConditionalOpCode opCode,
    								final int opCodePointer ) {
    		this.variable	   = variable;
    		this.opCode         = opCode;
    		this.opCodePointer  = opCodePointer;
    }

    /**
     * Checks to see whether the final condition has been satisified
     * @param compiledCode the currently running {@link JBasicCompiledCode compiled code}
     * @return true, if the condition has been satified
     */
    public boolean conditionSatisfied( JBasicCompiledCode compiledCode ) 
    throws JBasicException {
    		return opCode.conditionSatisfied( compiledCode );
    }

    /**
	 * @return the variable.
	 */
	public Variable getVariable() {
		return variable;
	}
    
    /**
     * @return the code position.
     */
    public int getOpCodePointer() {
    		return opCodePointer;
    }

}