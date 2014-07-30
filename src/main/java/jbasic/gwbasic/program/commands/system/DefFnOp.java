package jbasic.gwbasic.program.commands.system;


import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.VariableReference;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

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
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute(JBasicCompiledCode program)
	throws JBasicException {
		throw new NotYetImplementedException();
	}

}
