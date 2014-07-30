package jbasic.gwbasic.program.commands.graphics;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * PAINT Statement
 * Purpose: To fill in a graphics figure with the selected attribute.
 * Syntax: PAINT (x start,y start)[,paint attribute[,border attribute][,bckgrnd attribute]]
 * @author lawrence.daniels@gmail.com
 */
public class PaintOp extends GwBasicCommand {
	
	/** 
	 * Creates an instance of tis opCode
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public PaintOp( TokenIterator it ) {
		// TODO Parse the arguments
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
