package org.ldaniels528.javapc.jbasic.common.tokenizer;

/**
 * Represents a parsed token
 * @author lawrence.daniels@gmail.com
 */
public interface Token {
	public static final int SEQUENCE 		= 0;
	public static final int COMMENTS 		= 1;
	public static final int DELIMITER 	= 2;
	public static final int EOL			= 3;
	public static final int NUMERIC		= 4;
	public static final int OPERATOR		= 5;
	public static final int QUOTED_TEXT	= 6;
	public static final int TEXT			= 7; 

  //////////////////////////////////////////
  //      Content-related Method(s)
  //////////////////////////////////////////

  /**
   * @return the content representing this token
   */
  public String getContent();

  /**
   * @return the type of this token
   */
  public int getType();

  //////////////////////////////////////////
  //      Position-related Method(s)
  //////////////////////////////////////////

  /**
   * @return the line number where this token occured
   */
  public int lineNumber();

  /** 
   * @return the length of this token
   */
  public int getLength();

  /**
   * @return the starting position of this token from the source string
   */
  public int getStart();

  /**
   * @return the ending position of this token from the source string
   */
  public int getEnd();

  //////////////////////////////////////////
  //      Indicator Method(s)
  //////////////////////////////////////////

  /** 
   * @return true, if this token is a comment
   */
  public boolean isComment();

  /** 
   * @return true, if this token is a delimiter
   */
  public boolean isDelimiter();

  /** 
   * @return true, if this token is an EOL indicator
   */
  public boolean isEOL();

  /** 
   * @return true, if this token is a sequence
   */
  public boolean isSequence();

  /** 
   * @return true, if this token is numeroc
   */
  public boolean isNumeric();

  /** 
   * @return true, if this token is an operator
   */
  public boolean isOperator();

  /** 
   * @return true, if this token is a quote text
   */
  public boolean isQuotedText();

  /** 
   * @return true, if this token is text
   */
  public boolean isText();

}