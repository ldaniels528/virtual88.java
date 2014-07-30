package jbasic.gwbasic.program.commands.control;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.ConditionalControlBlock;
import jbasic.common.program.ConditionalOpCode;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableReference;
import jbasic.common.values.impl.SimpleConstant;
import jbasic.common.values.types.impl.JBasicTempNumber;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * FOR Command 
 * <br>Syntax: FOR <i>var</i> = <i>value1</i> TO <i>value2</i> [STEP <i>value3</i>]
 * <br>Example: FOR I = 10 TO 1 STEP -1 
 * @see ConditionalControlBlock
 * @see NextOp
 */
public class ForOp extends GwBasicCommand implements ConditionalOpCode {
	private final VariableReference reference;
	private final Value initialValue;
	private final Value finalValue;
	private final Value stepValue;

	/**
	 * Creates an instance of this opCode
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws JBasicException
	 */
	public ForOp( final TokenIterator it ) 
	throws JBasicException {
		// if there are no more tokens, syntax error ...
		if (!it.hasNext())
			throw new SyntaxErrorException();

		// get the variable reference
		reference = GwBasicValues.getVariableReference(it);

		// next token should be '='
		JBasicTokenUtil.mandateToken(it, "=" );

		// get the next value (constant or value)
		initialValue = GwBasicValues.getValueReference(it);

		// next token should be 'TO'
		JBasicTokenUtil.mandateToken(it, "TO");

		// get the next value (constant or value)
		finalValue = GwBasicValues.getValueReference(it);

		// are there more tokens
		if( it.hasNext() ) {
			// next token must be 'STEP'
			JBasicTokenUtil.mandateToken(it, "STEP");

			// get step value
			stepValue = GwBasicValues.getValueReference(it);

			// there should be no more tokens
			JBasicTokenUtil.noMoreTokens(it);
		}

		// set default step value
		else {
			stepValue = new SimpleConstant( new JBasicTempNumber(1) );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jbasic.base.program.ConditionalOpCode#conditionSatisfied(jbasic.base.environment.JBasicEnvironment)
	 */
	@SuppressWarnings("unchecked")
	public boolean conditionSatisfied( final JBasicCompiledCode program )
	throws JBasicException {
		// lookup the control variable
		final Variable variable = reference.getVariable(program);

		// get a reference to the variable's content
		final NumberMemoryObject number = (NumberMemoryObject)variable.getValue(program);
		
		// get the "step" value
		final NumberMemoryObject step = (NumberMemoryObject)stepValue.getValue(program);

		// increment/decrement the value	
		final NumberMemoryObject newValue = number.add( step ); 
		variable.setValue( newValue );		

		// return result
		final MemoryObject objectA = variable.getValue(program);
		final MemoryObject objectB = finalValue.getValue(program);
		final int result = objectA.compareTo( objectB );

		// indicate whether the loop should contine
		return ( step.toDoublePrecision() > 0 ) ? result > 0 : result < 0;
	}

	/*
	 * (non-Javadoc) 
	 * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram,
	 *      jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode compiledCode ) 
	throws JBasicException {
		// lookup the variable
		final Variable variable = reference.getVariable( compiledCode );
		
		// set the variable with the initial value
		variable.setValue(initialValue.getValue( compiledCode ) );
		
		// start a condition control session
		compiledCode.conditionalControlBegin( variable, this );
	}
	
	  /* 
	   * (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	  public String toString() {
		  return "FOR";
	  }	  	 

}
