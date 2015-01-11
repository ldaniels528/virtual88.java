package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/** 
 * AUTO Command
 * <br>Purpose: To generate and increment line numbers automatically each 
 * time you press the RETURN key.
 * <pre>
 * Syntax: AUTO [line number][,[increment]]
 *		   AUTO .[,[increent]]
 *</pre>
 * @author lawrence.daniels@gmail.com
 */
public class AutoOp extends GwBasicCommand {

	/**
	 * Default constructor
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public AutoOp( TokenIterator it ) throws JBasicException {
		throw new NotYetImplementedException();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		// TODO Auto-generated method stub		
	}

}
