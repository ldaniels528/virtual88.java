package jbasic.gwbasic.program.commands.ide;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		// TODO Auto-generated method stub		
	}

}
