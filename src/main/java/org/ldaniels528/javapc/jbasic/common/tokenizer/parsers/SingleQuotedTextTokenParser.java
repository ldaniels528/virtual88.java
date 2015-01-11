package org.ldaniels528.javapc.jbasic.common.tokenizer.parsers;

import org.ldaniels528.javapc.jbasic.common.tokenizer.SimpleToken;
import org.ldaniels528.javapc.jbasic.common.tokenizer.Token;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenParser;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenizerContext;

/**
 * Parses single quoted-text tokens
 * @author lawrence.daniels@gmail.com
 */
public class SingleQuotedTextTokenParser implements TokenParser {

	/*
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.util.tokenizer.TokenParser#getToken(org.ldaniels528.javapc.jbasic.util.tokenizer.TokenizerContext)
	 */
	public Token getToken( final TokenizerContext ctx ) {
		if( ctx.exprCh[ctx.position] == '\'' ) {
			// record the quote character
			final char quote = ctx.exprCh[ctx.position];
			
			// record the start of the token
			final int start = ctx.position;
			
			// gather text until an ending quote is met
			while (++ctx.position < ctx.exprCh.length
					&& ctx.exprCh[ctx.position] != quote);
			
			// advance one more position for the quote character
			ctx.position++;
			
			// return the token
			return new SimpleToken(ctx.expr.substring(start,
					(ctx.position < ctx.exprCh.length) ? ctx.position
							: ctx.exprCh.length), Token.QUOTED_TEXT, start,
					ctx.position, ctx.lineNo);
		}
		return null;
	}

}
