package jbasic.gwbasic.program.commands.control;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) throws JBasicException {
		throw new NotYetImplementedException();	
	}

}
