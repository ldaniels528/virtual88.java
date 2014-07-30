package com.ldaniels528.tokenizer.parsers;

import com.ldaniels528.tokenizer.SimpleToken;
import com.ldaniels528.tokenizer.Token;
import com.ldaniels528.tokenizer.TokenParser;
import com.ldaniels528.tokenizer.TokenizerContext;

/**
 * Parses operator tokens
 * @author lawrence.daniels@gmail.com
 */
public class OperatorTokenParser implements TokenParser {
	private static final String OPERATORS   = "+-*/^=<>!";
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.util.tokenizer.TokenParser#getToken(jbasic.util.tokenizer.TokenizerContext)
	 */
	public Token getToken( final TokenizerContext ctx ) {
		// if the current character is an operator ...
	    if( OPERATORS.indexOf( ctx.exprCh[ ctx.position ] ) != -1 ) {
	        	// record start position
	    		final int start = ctx.position ++;
	        
	        // check for "<>" symbol
	        if( ( ctx.exprCh[ start ] == '<' ) && 
	      		  ( ctx.position < ctx.exprCh.length ) && 
	      		  ( ctx.exprCh[ ctx.position ] == '>' ) ) ctx.position++;
	        
	        // return the new tokem
	        return new SimpleToken( ctx.expr.substring( start, ctx.position  ), Token.OPERATOR, start, ctx.position, ctx.lineNo );
	    }
	    else return null;
	}

}
