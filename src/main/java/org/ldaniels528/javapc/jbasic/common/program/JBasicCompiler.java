package org.ldaniels528.javapc.jbasic.common.program;

import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * JBasic Compiler
 * @author lawrence.daniels@gmail.com
 */
public interface JBasicCompiler {
	
	/**
	 * Compiles the resident program into {@link GwBasicCommand opCodes}
	 * @param program the given {@link JBasicSourceCode program}
	 * @return {@link JBasicCompiledCode compiled code}
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	JBasicCompiledCode compile( JBasicSourceCode program ) 
	throws JBasicException;
	
	/**
	 * Compiles the given statement
	 * @param environment the given {@link IbmPcSystem environment}
	 * @param statement the given {@link JBasicProgramStatement statement}
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	JBasicCompiledCode compile( IbmPcSystem environment, JBasicProgramStatement statement )
	throws JBasicException;
	
	/**
	 * Translates the given iteration of tokens into opCodes
	 * @param it the given {@link TokenIterator iteration of tokens}
	 * @return the resultant {@link OpCode opCode}
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	OpCode compile( TokenIterator it ) 
	throws JBasicException;
	
}
