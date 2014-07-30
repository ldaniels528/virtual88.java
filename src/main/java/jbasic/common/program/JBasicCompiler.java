package jbasic.common.program;

import ibmpc.system.IbmPcSystem;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * JBasic Compiler
 * @author lawrence.daniels@gmail.com
 */
public interface JBasicCompiler {
	
	/**
	 * Compiles the resident program into {@link GwBasicCommand opCodes}
	 * @param program the given {@link JBasicSourceCode program}
	 * @return {@link JBasicCompiledCode compiled code}
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	JBasicCompiledCode compile( JBasicSourceCode program ) 
	throws JBasicException;
	
	/**
	 * Compiles the given statement
	 * @param environment the given {@link IbmPcSystem environment}
	 * @param statement the given {@link JBasicProgramStatement statement}
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	JBasicCompiledCode compile( IbmPcSystem environment, JBasicProgramStatement statement )
	throws JBasicException;
	
	/**
	 * Translates the given iteration of tokens into opCodes
	 * @param it the given {@link TokenIterator iteration of tokens}
	 * @return the resultant {@link OpCode opCode}
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	OpCode compile( TokenIterator it ) 
	throws JBasicException;
	
}
