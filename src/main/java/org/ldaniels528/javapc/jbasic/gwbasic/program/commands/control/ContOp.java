package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * CONT Command
 * <br>Purpose: To continue program execution after a break. 
 * <br>Syntax: CONT
 * @author lawrence.daniels@gmail.com
 * NotYetImplementedException
 */
public class ContOp extends GwBasicCommand {

	/**
	 * Default constructor
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public ContOp( TokenIterator it ) throws JBasicException {
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) throws JBasicException {
		throw new NotYetImplementedException();	
	}

}
