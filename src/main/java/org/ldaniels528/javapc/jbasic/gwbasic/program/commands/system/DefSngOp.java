package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.values.VariableTypeDefinition;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * DEFSNG Command 
 * <br>Purpose: To declare variable types as integer, single-precision, 
 * double-precision, or string.
 * <br>Syntax: DEFSNG <i>m</i>-<i>n</i> 
 * <br>Example: DEFSNG A-Z
 */
public class DefSngOp extends AbstractDefXxxOp {

	/**
	 * Creates an instance of this opCode 
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	public DefSngOp(TokenIterator it) throws JBasicException {
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
		final VariableTypeDefinition typeDef = new VariableTypeDefinition( label1, label2, VariableTypeDefinition.TYPE_SINGLE_PREC ); 		
		
		// add the variable type definition to the compiled code
		compiledCode.add( typeDef );
	}

}
