package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * COM(n) Statement
 * <br>Purpose: To enable or disable trapping of communications 
 * activity to the specified communications adapter.
 * <pre>
 * Syntax: COM(n) ON
 *		   COM(n) OFF
 *		   COM(n) STOP
 *</pre>
 * @author lawrence.daniels@gmail.com
 */
public class ComOp extends GwBasicCommand {

	/**
	 * Default constructor
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public ComOp( TokenIterator it ) throws JBasicException {
		throw new NotYetImplementedException();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) throws JBasicException {
		throw new NotYetImplementedException();		
	}
	
}
