package jbasic.gwbasic.program.commands.system;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

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
	 * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		throw new NotYetImplementedException();
	}

}
