package jbasic.gwbasic.program.commands.control;


import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode program ) 
	throws JBasicException {
		throw new NotYetImplementedException();	
	}

}
