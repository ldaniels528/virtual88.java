package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/** 
 * SHELL Statement
 * <br>Purpose: To load and execute another program or batch file. 
 * When the program finishes, control returns to the GW-BASIC program 
 * at the statement following the SHELL statement. A program executed 
 * under control of GW-BASIC is referred to as a child process.
 * <br>Syntax: SHELL [<i>string</i>]
 * @author lawrence.daniels@gmail.com
 */
public class ShellOp extends GwBasicCommand {
	
	/**
	 * Creates an instance of this opCode
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException 
	 */
	public ShellOp( TokenIterator it ) throws JBasicException {
		if( it.hasNext() ) {
			GwBasicValues.getValue( it );
			JBasicTokenUtil.nextToken( it );
		}
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
