package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.values.VariableTypeDefinition;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * DEFINT Command 
 * <br>Purpose: To declare variable types as integer, single-precision, 
 * double-precision, or string.
 * <br>Syntax: DEFINT <i>m</i>-<i>n</i> 
 * <br>Example: DEFINT A-Z
 */
public class DefIntOp extends AbstractDefXxxOp {

	/**
	 * Creates an instance of this opCode 
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	public DefIntOp(TokenIterator it) throws JBasicException {
		super(it);
	}

	/*
	 * (non-Javadoc) 
	 * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram,
	 *      org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode compiledCode )
	throws JBasicException {
		// create the variable type definition
		final VariableTypeDefinition typeDef = new VariableTypeDefinition( label1, label2, VariableTypeDefinition.TYPE_INTEGER ); 		
		
		// add the variable type definition to the compiled code
		compiledCode.add( typeDef );
	}

}
