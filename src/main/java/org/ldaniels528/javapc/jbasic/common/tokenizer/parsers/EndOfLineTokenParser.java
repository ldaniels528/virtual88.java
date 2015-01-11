package org.ldaniels528.javapc.jbasic.common.tokenizer.parsers;

import org.ldaniels528.javapc.jbasic.common.tokenizer.SimpleToken;
import org.ldaniels528.javapc.jbasic.common.tokenizer.Token;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenParser;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenizerContext;

/**
 * Parses End of Line (EOL) tokens (e.g. '\n')
 * @author lawrence.daniels@gmail.com
 */
public class EndOfLineTokenParser implements TokenParser {

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.util.tokenizer.TokenParser#getToken(org.ldaniels528.javapc.jbasic.util.tokenizer.TokenizerContext)
	 */
	public Token getToken( final TokenizerContext ctx ) {
	    if( ctx.exprCh[ ctx.position ] == '\n' )
	        return new SimpleToken( String.valueOf( ctx.exprCh[ ctx.position ] ), Token.EOL, ctx.position, ++ctx.position, ++ctx.lineNo );
	      else
	        return null;
	}	

}
