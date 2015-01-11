package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/** 
 * TRON Command
 * Purpose: To trace the execution of program statements.
 * Syntax: TRON
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide.TraceOffOp
 * @author lawrence.daniels@gmail.com
 */
public class TraceOnOp extends GwBasicCommand {	

	/**
	 * Default constructor
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public TraceOnOp( TokenIterator it ) throws JBasicException {
		JBasicTokenUtil.noMoreTokens( it );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode compiledCode ) 
	throws JBasicException {
		// get the GWBASIC-specific compiled code instance
		final GwBasicCompiledCode gwCompiledCode = (GwBasicCompiledCode)compiledCode;
		
		// turn trace off
		gwCompiledCode.setTrace( true );
	}

}
