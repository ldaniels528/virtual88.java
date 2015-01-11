package org.ldaniels528.javapc.jbasic.common.program;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;

/**
 * JBasic Runtime
 * @author lawrence.daniels@gmail.com
 */
public interface JBasicRuntime {
	
	/**
	 * Evaluates (compiled and executes) the given statement
	 * @param program the given {@link JBasicSourceCode program}
	 * @param statement the given {@link JBasicProgramStatement statement}
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	void evaluate( JBasicSourceCode program, JBasicProgramStatement statement )
	throws JBasicException;
	
	/**
	 * Runs the program that is currently resident in memory
	 * @param program the given {@link JBasicSourceCode program}
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	void run( JBasicSourceCode program ) throws JBasicException;
	
}
