package jbasic.gwbasic.program.commands.io;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) throws JBasicException {
		throw new NotYetImplementedException();		
	}
	
}
