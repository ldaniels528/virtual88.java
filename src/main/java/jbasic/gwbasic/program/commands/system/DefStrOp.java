package jbasic.gwbasic.program.commands.system;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;

/**
 * DEFSTR Command 
 * <br>Purpose: To declare variable types as integer, single-precision, 
 * double-precision, or string.
 * <br>Syntax: DEFSTR <i>m</i>-<i>n</i> 
 * <br>Example: DEFSTR A-Z
 */
public class DefStrOp extends AbstractDefXxxOp {

	/**
	 * Creates an instance of this opCode 
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	public DefStrOp(TokenIterator it) throws JBasicException {
		super(it);
	}

	/*
	 * (non-Javadoc) 
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram,
	 *      jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program)
	throws JBasicException {
		// TODO Determine how to implement this
	}

}
