package com.ldaniels528.tokenizer.parsers;

import com.ldaniels528.tokenizer.Token;

/**
 * Represents a token sequence; a block sequence of characters
 * @author lawrence.daniels@gmail.com
 */
public class TokenSequence {
  private final int tokenType;
  private final String startSymbol;
  private final String endSymbol;

  public TokenSequence( String startSymbol, String endSymbol ) {
    this( startSymbol, endSymbol, Token.SEQUENCE );
  }

  public TokenSequence( String startSymbol, String endSymbol, int tokenType ) {
    this.startSymbol  = startSymbol;
    this.endSymbol    = endSymbol;
    this.tokenType    = tokenType;
  }

  public String getEnd() {
    return endSymbol;
  }

  public String getStart() {
    return startSymbol;
  }

  public int getTokenType() {
    return tokenType;
  }

}
