package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.device;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		throw new NotYetImplementedException();
	}

}
