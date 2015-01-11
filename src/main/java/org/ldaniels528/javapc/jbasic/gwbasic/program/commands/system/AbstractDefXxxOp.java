package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * DEFxxx Command 
 * <br>Syntax: DEFxxx <i>m</i>-<i>n</i> 
 * <br>Example: DEFxxx A-Z
 */
public abstract class AbstractDefXxxOp extends GwBasicCommand {
	protected final char label1;
	protected final char label2;

	/**
	 * Creates an instance of this opCode 
	 * @param it the parsed text that describes the BASIC instruction
	 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	 */
	public AbstractDefXxxOp(TokenIterator it) 
	throws JBasicException {
		// extract the tokens we need
		final TokenIterator args = JBasicTokenUtil.extractTokens(it, "@@", "-", "@@");
		final String s1 = JBasicTokenUtil.nextToken(args);		
		final String s2 = JBasicTokenUtil.nextToken(args);
		JBasicTokenUtil.noMoreTokens(it);
		
		// validate our findings
		if( s1.length() > 1 || s2.length() > 1 )
			throw new SyntaxErrorException();
		
		// get the labels
		this.label1 = s1.charAt( 0 );
		this.label2 = s2.charAt( 0 );		
	}

}
