package com.ldaniels528.tokenizer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Low-Level Language Parser; Well suited for parsing scripting languages
 */
public class Tokenizer {  
  private static final String WHITESPACE  = " \r\t";
  private final Collection<TokenParser> parsers;

  /**
   * Creates an instance of this tokenizer
   */
  public Tokenizer() {
	  this.parsers = new LinkedList<TokenParser>();
  }

  /**
   * Parses the given text
   * @param text the given text to be parsed
   */
  public TokenizerContext parse( String text ) {
	  return new TokenizerContext( text );
  }
  
  /**
   * Returns the remaining elements as an iterator
   * @param context the given {@link TokenizerContext context}
   * @return the remaining elements as an {@link TokenIterator iterator}
   */
  public TokenIterator nextTokens( TokenizerContext context ) {	  	  
	  final List<String> tokens = new LinkedList<String>();
	  Token token;
	  while( ( token = nextToken( context ) ) != null ) {
		  tokens.add( token.getContent() );
	  }
	  return new TokenIterator( tokens );
  }

  /**
   * @return the next {@link Token token} found in the values
   */
  public Token nextToken( TokenizerContext ctx ) {
	Token token = null;
	
	// skip white space?
	if( ctx.ignoreWhiteSpace ) { 
	    while( ( ctx.position < ctx.exprCh.length ) &&
	           ( WHITESPACE.indexOf( ctx.exprCh[ ctx.position ] ) != -1 ) ) ctx.position++;
	}
	
	// check for end-of-feed
	if( ctx.position >= ctx.exprCh.length ) return null;
	
	// allow each parser an opportunity to extract the context
	for( TokenParser parser : parsers ) {
	    if( ( token = parser.getToken( ctx ) ) != null ) return token;
	}
	
	// must be delimeter
	return new SimpleToken( String.valueOf( ctx.exprCh[ ctx.position ] ), Token.DELIMITER, ctx.position, ++ctx.position, ctx.lineNo );
  }
  
  /**
   * Adds a {@link TokenParser token parser} to this tokenizer
 * @param sequence the given {@link TokenParser token parser}
   */
  public void add( TokenParser parser ) {
	  parsers.add( parser );
  }
  
  /**
   * Removes the given token parser from this tokenizer
   * @param tokenType the given token type
   * @return true, if the token parser was removed
   */
  public boolean removeParser( TokenParser parser ) {
	  return parsers.remove( parser );
  }

}