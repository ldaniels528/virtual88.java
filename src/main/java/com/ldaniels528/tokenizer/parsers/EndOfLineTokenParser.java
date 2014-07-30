package com.ldaniels528.tokenizer.parsers;

import com.ldaniels528.tokenizer.SimpleToken;
import com.ldaniels528.tokenizer.Token;
import com.ldaniels528.tokenizer.TokenParser;
import com.ldaniels528.tokenizer.TokenizerContext;

/**
 * Parses End of Line (EOL) tokens (e.g. '\n')
 * @author lawrence.daniels@gmail.com
 */
public class EndOfLineTokenParser implements TokenParser {

	/* 
	 * (non-Javadoc)
	 * @see jbasic.util.tokenizer.TokenParser#getToken(jbasic.util.tokenizer.TokenizerContext)
	 */
	public Token getToken( final TokenizerContext ctx ) {
	    if( ctx.exprCh[ ctx.position ] == '\n' )
	        return new SimpleToken( String.valueOf( ctx.exprCh[ ctx.position ] ), Token.EOL, ctx.position, ++ctx.position, ++ctx.lineNo );
	      else
	        return null;
	}	

}
