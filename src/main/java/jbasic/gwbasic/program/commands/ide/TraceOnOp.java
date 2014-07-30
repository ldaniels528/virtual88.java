package jbasic.gwbasic.program.commands.ide;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.GwBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

import com.ldaniels528.tokenizer.TokenIterator;

/** 
 * TRON Command
 * Purpose: To trace the execution of program statements.
 * Syntax: TRON
 * @see jbasic.gwbasic.program.commands.ide.TraceOffOp
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
	 * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode compiledCode ) 
	throws JBasicException {
		// get the GWBASIC-specific compiled code instance
		final GwBasicCompiledCode gwCompiledCode = (GwBasicCompiledCode)compiledCode;
		
		// turn trace off
		gwCompiledCode.setTrace( true );
	}

}