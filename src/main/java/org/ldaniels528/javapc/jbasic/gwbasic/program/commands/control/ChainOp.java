package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;


import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * CHAIN Command
 * <br>Purpose: To transfer control to the specified program 
 * and pass (chain) variables to it from the current program.
 * <br>Syntax: CHAIN[MERGE] filename[,[line][,[ALL][,DELETE range]]]
 * @author lawrence.daniels@gmail.com
 */
public class ChainOp extends GwBasicCommand {
	private final boolean merge;
	private final Value filename;
	private int[] deleteRange;
	private int line;
	private boolean all;	

	/**
	 * Default constructor
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public ChainOp( final TokenIterator it ) throws JBasicException {
		// get merge parameter
		this.merge = "MERGE".equals( it.peekAtNext() );
		if( merge ) it.next();
		
		// get all other parameters
		Value[] params = JBasicTokenUtil.parseValues( it, 1, 5 );
		
		// get filename parameter
		this.filename = params[0];
	
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode program ) 
	throws JBasicException {
		throw new NotYetImplementedException();	
	}

}
