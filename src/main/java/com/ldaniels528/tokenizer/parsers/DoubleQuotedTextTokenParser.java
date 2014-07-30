package com.ldaniels528.tokenizer.parsers;

import com.ldaniels528.tokenizer.SimpleToken;
import com.ldaniels528.tokenizer.Token;
import com.ldaniels528.tokenizer.TokenParser;
import com.ldaniels528.tokenizer.TokenizerContext;

/**
 * Parses double quoted-text tokens
 * @author lawrence.daniels@gmail.com
 */
public class DoubleQuotedTextTokenParser implements TokenParser {

	/*
	 * (non-Javadoc)
	 * @see jbasic.util.tokenizer.TokenParser#getToken(jbasic.util.tokenizer.TokenizerContext)
	 */
	public Token getToken( final TokenizerContext ctx ) {
		if( ctx.exprCh[ctx.position] == '"' ) {
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
