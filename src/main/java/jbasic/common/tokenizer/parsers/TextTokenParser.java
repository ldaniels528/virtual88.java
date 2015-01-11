package jbasic.common.tokenizer.parsers;

import jbasic.common.tokenizer.SimpleToken;
import jbasic.common.tokenizer.Token;
import jbasic.common.tokenizer.TokenParser;
import jbasic.common.tokenizer.TokenizerContext;

/**
 * Parses alphanumeric text and identifiers
 * @author lawrence.daniels@gmail.com
 *
 */
public class TextTokenParser implements TokenParser {

	/* 
	 * (non-Javadoc)
	 * @see jbasic.tokenizer.TokenParser#getToken(jbasic.tokenizer.TokenizerContext)
	 */
	public Token getToken( TokenizerContext ctx ) {
		final char[] exprCh = ctx.exprCh;
		
		// check for a positive number or alphanumeric value 
		if( Character.isLetterOrDigit( exprCh[ ctx.position ] ) ) 
			return parseAlphaOrNumericText( ctx );
		
		// not recognized
		else return null;
	}
	
	/**
	 * Parses alphanumeric text
	 * @param ctx the given {@link TokenizerContext tokenizer context}
	 * @return the {@link Token token} representing the alphanumeric text
	 */
	private Token parseAlphaOrNumericText( final TokenizerContext ctx ) {
		final char[] exprCh = ctx.exprCh;
		
		// get the starting context.position
		final int start = ctx.position ++;
  
		// get any occuring letters or digits
		while( ( ctx.position < exprCh.length ) &&
				( Character.isLetterOrDigit( exprCh[ ctx.position ] ) ||
				  exprCh[ ctx.position ] == '_' ||
				  exprCh[ ctx.position ] == '.'  ) ) ctx.position ++;
  
		// check for GWBASIC specific endings
		if( Character.isLetter( exprCh[start] ) && 
		  	( ctx.position < exprCh.length ) &&
		  	(  exprCh[ ctx.position ] == '$' ||
		  	   exprCh[ ctx.position ] == '%' ||
		  	   exprCh[ ctx.position ] == '#' ||
		  	   exprCh[ ctx.position ] == '!' ) ) ctx.position ++;
  
		// extract the string
		final String s =  ctx.expr.substring( start, ctx.position  );
  
		// return the token
		return new SimpleToken( s, Token.TEXT, start, ctx.position, ctx.lineNo );
	}

}
