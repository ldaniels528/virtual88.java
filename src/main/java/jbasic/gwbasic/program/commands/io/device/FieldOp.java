package jbasic.gwbasic.program.commands.io.device;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * FIELD Command
 * <br>Purpose: To allocate space for variables in a random file buffer.
 * <br>Syntax: FIELD [#] filenum, width AS stringvar [,width AS stringvar]...
 * @author lawrence.daniels@gmail.com
 */
public class FieldOp extends GwBasicCommand {

	/**
	 * Creates an instance of this opCode
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws JBasicException
	 */
	public FieldOp( TokenIterator it ) {
		// TODO Need to parse this instruction
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		throw new NotYetImplementedException();
	}

}
