package jbasic.gwbasic.program.commands.system;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.values.VariableTypeDefinition;

import com.ldaniels528.tokenizer.TokenIterator;

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
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	public DefIntOp(TokenIterator it) throws JBasicException {
		super(it);
	}

	/*
	 * (non-Javadoc) 
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram,
	 *      jbasic.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode compiledCode )
	throws JBasicException {
		// create the variable type definition
		final VariableTypeDefinition typeDef = new VariableTypeDefinition( label1, label2, VariableTypeDefinition.TYPE_INTEGER ); 		
		
		// add the variable type definition to the compiled code
		compiledCode.add( typeDef );
	}

}
