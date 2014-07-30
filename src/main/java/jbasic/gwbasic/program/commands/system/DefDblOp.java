package jbasic.gwbasic.program.commands.system;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.values.VariableTypeDefinition;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * DEFDBL Command 
 * <br>Purpose: To declare variable types as integer, single-precision, 
 * double-precision, or string.
 * <br>Syntax: DEFDBL <i>m</i>-<i>n</i> 
 * <br>Example: DEFDBL A-Z
 */
public class DefDblOp extends AbstractDefXxxOp {

	/**
	 * Creates an instance of this opCode 
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	public DefDblOp( final TokenIterator it ) 
	throws JBasicException {
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
		final VariableTypeDefinition typeDef = new VariableTypeDefinition( label1, label2, VariableTypeDefinition.TYPE_DOUBLE_PREC ); 		
		
		// add the variable type definition to the compiled code
		compiledCode.add( typeDef );
	}

}
