package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;


import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * DEF FN Command
 * <br>DEF FN<i>name</i>[(<i>arguments</i>)]=<i>expression</i>
 * <br>Example: DEF FNLOG(base,number)=LOG(number)/LOG(base)
 */
public class DefFnOp extends GwBasicCommand {
	private final VariableReference reference;
	private final String funcName;

	/**
	 * Creates an instance of this opCode
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws JBasicException
	 */
	public DefFnOp(TokenIterator it) throws JBasicException {
		// get the function name
		funcName = JBasicTokenUtil.nextToken(it);

		// next token must be '('
		JBasicTokenUtil.mandateToken( it, "(" );

		// get reference variable
		reference = GwBasicValues.getVariableReference(it);

		// next token must be ')'
		JBasicTokenUtil.mandateToken( it, ")" );

		// next token must be '='
		JBasicTokenUtil.mandateToken( it, "=" );
		
		// parse the expression
		// TODO Figure out how to parse the expression
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program)
	throws JBasicException {
		throw new NotYetImplementedException();
	}

}
