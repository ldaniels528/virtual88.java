package com.ldaniels528.tokenizer.parsers;

import com.ldaniels528.tokenizer.SimpleToken;
import com.ldaniels528.tokenizer.Token;
import com.ldaniels528.tokenizer.TokenParser;
import com.ldaniels528.tokenizer.TokenizerContext;

/**
 * Parses numeric tokens (binary, decimal, hexadecimal, and octal)
 * @author lawrence.daniels@gmail.com
 */
public class NumericTokenParser implements TokenParser {
	//private static final String DEC_NUMBERS = "0123456789";
	private static final String HEX_NUMBERS = "0123456789ABCDEF";
	private static final String OCT_NUMBERS = "01234567";

	/**
	 * Extracts numeric representations
	 * @param ctx the given {@link TokenizerContext tokenizer context}
	 * @return a {@link com.ldaniels528.tokenizer.Token token} representing the letters or digits
	 */
	public Token getToken( final TokenizerContext ctx ) {
		final char[] exprCh = ctx.exprCh;
		
		// check for GWBASIC hexadecimal number
		if( ( exprCh[ ctx.position ] == '&' ) &&
			( ctx.position + 1 < exprCh.length ) && 
			( exprCh[ ctx.position + 1 ] == 'H' ) ) 
			return parseHexadecimalNumber( ctx );
		
		// check for GWBASIC octal number
		else if( ( exprCh[ ctx.position ] == '&' ) &&
			( ctx.position + 1 < exprCh.length ) && 
			( exprCh[ ctx.position + 1 ] == 'O' ||
			  OCT_NUMBERS.indexOf( exprCh[ ctx.position+1 ] ) != -1 ) ) 
			return parseOctalNumber( ctx );
		  
		// check for a positive number or alphanumeric value 
		else if( Character.isDigit( exprCh[ ctx.position ] ) ) 
			return parsePositiveNumber( ctx );
		
		// not recognized
		else return null;
	}
	
	/**
	 * Parses a GWBASIC specific hexadecimal number
	 * @param ctx the given {@link TokenizerContext tokenizer context}
	 * @return the {@link Token token} representing the hexadecimal number
	 */
	private Token parseHexadecimalNumber( final TokenizerContext ctx ) {
		final char[] exprCh = ctx.exprCh;
		
		// get the starting position
		final int start = ctx.position;
		
		// advance two positions
		ctx.position += 2;
		
		// get all numbers 
		while( ( ctx.position < exprCh.length ) && 
				HEX_NUMBERS.indexOf( exprCh[ ctx.position ] ) != -1 ) ctx.position++;
		
		// extract the string
		final String str =  ctx.expr.substring( start, ctx.position );
		
		// return the token
		return new SimpleToken( str, Token.NUMERIC, start, ctx.position, ctx.lineNo );
	}
	
	/**
	 * Parses a GWBASIC specific octal number
	 * @param ctx the given {@link TokenizerContext tokenizer context}
	 * @return the {@link Token token} representing the octal number
	 */
	private Token parseOctalNumber( final TokenizerContext ctx ) {
		final char[] exprCh = ctx.exprCh;
		
		// get the starting position
		final int start = ctx.position;
		
		// advance two positions
		ctx.position += 2;
		
		// get all numbers 
		while( ( ctx.position < exprCh.length ) && 
				OCT_NUMBERS.indexOf( exprCh[ ctx.position ] ) != -1 ) ctx.position++;
		
		// extract the string
		final String str =  ctx.expr.substring( start, ctx.position );
		
		// return the token
		return new SimpleToken( str, Token.NUMERIC, start, ctx.position, ctx.lineNo );
	}
	
	/**
	 * Parses alphanumeric text
	 * @param ctx the given {@link TokenizerContext tokenizer context}
	 * @return the {@link Token token} representing the alphanumeric text
	 */
	private Token parsePositiveNumber( final TokenizerContext ctx ) {
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
		  	(  exprCh[ ctx.position ] == '%' ||
		  	   exprCh[ ctx.position ] == '#' ||
		  	   exprCh[ ctx.position ] == '!' ) ) ctx.position ++;
  
		// extract the string
		final String s =  ctx.expr.substring( start, ctx.position  );
  
		// return the token
		return new SimpleToken( s, Token.NUMERIC, start, ctx.position, ctx.lineNo );
	}

}
