package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

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
	 * @see org.ldaniels528.javapc.jbasic.program.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram,
	 *      org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
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
