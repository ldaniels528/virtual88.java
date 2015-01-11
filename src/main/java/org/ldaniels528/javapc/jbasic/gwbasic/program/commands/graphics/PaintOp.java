package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.graphics;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		throw new NotYetImplementedException();		
	}

}
