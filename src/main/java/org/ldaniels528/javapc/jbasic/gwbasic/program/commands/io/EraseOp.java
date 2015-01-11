package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;


import java.util.Collection;
import java.util.LinkedList;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/** 
 * ERASE Statement
 * <br>Purpose: To eliminate arrays from a program.
 * <br>Syntax: ERASE <i>list of array variables</i>
 * @author lawrence.daniels@gmail.com
 */
public class EraseOp extends GwBasicCommand {
	private final Collection<String> arrays;

	/** 
	 * Creates an instance of this opCode
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException 
	 */ 
	public EraseOp( TokenIterator it ) throws JBasicException {
		this.arrays = parse( it );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program) 
	throws JBasicException {
		// deallocate the set of arrays
		for( final String arrayName : arrays ) {
			program.destroyVariableArray( arrayName );
		}
	}
	
	/** 
	 * Parses the names of the arrays to deallocate
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException 
	 */
	private Collection<String> parse( TokenIterator it ) throws JBasicException {
		final Collection<String> references = new LinkedList<String>();
		while( it.hasNext() ) {
			// get the name of the array
			final String arrayName = JBasicTokenUtil.nextToken( it );
			
			// add it to our references
			references.add( arrayName );
			
			// if there's another token, must be a comma (,)
			if( it.hasNext() ) {
				JBasicTokenUtil.mandateToken( it, "," );
			}
		}
		return references;
	}

}
