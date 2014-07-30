package jbasic.gwbasic.program.commands.system;

import ibmpc.devices.memory.MemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * OPTION BASE Command
 * <br>Purpose: To declare the minimum value for array subscripts.
 * <br>Syntax: OPTION BASE <i>n</i> (<i>where n = 0|1</i>)
 */
public class OptionBaseOp extends GwBasicCommand {
	private final Value value;

	/**
	 * Creates an instance of this opCode
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws JBasicException
	 */
	public OptionBaseOp(TokenIterator it) throws JBasicException {
		JBasicTokenUtil.mandateToken( it, "BASE" );
		value = GwBasicValues.getValueReference( it );
		JBasicTokenUtil.noMoreTokens( it );
	}

	/*
	 * (non-Javadoc)	 
	 * @see jbasic.program.OpCode#execute(jbasic.program.JBasicProgram,
	 *      jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program)
	throws JBasicException {
		// get the value object
		final MemoryObject object = value.getValue( program );
		if( !object.isNumeric() )
			throw new TypeMismatchException( object );
		
		// get the Option Base value
		final int optionBase = object.toInteger();
		if( optionBase < 0 || optionBase > 1 )
			throw new IllegalFunctionCallException();
		
		// TODO Determine how to implement this
		throw new NotYetImplementedException();
	}

}
