package jbasic.common.tokenizer;

/**
 * Represents a simple {@link Token token} implementation
 */
public class SimpleToken implements Token {
  private final int type;
  private final String content;
  private final int lineNo;
  private final int start;
  private final int end;

  /**
   * Creates this token instance
   * @param content the content of the token
   * @param type the type of token
   * @param start the starting position of the token within the string
   * @param end the ending position of the token within the string
   * @param lineNo the line number that the token occurred within
   */
  public SimpleToken( String content, int type, int start, int end, int lineNo ) {
    this.content  = content;
    this.type     = type;
    this.start    = start;
    this.end      = end;
    this.lineNo   = lineNo;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#getContent()
   */
  public String getContent() {
    return this.content;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#getEnd()
   */
  public int getEnd() {
    return this.end;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#getLength()
   */
  public int getLength() {
    return content.length();
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#getStart()
   */
  public int getStart() {
    return start;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#getType()
   */
  public int getType() {
    return type;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isComment()
   */
  public boolean isComment() {
    return type == COMMENTS;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isDelimiter()
   */
  public boolean isDelimiter() {
    return type == DELIMITER;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isEOL()
   */
  public boolean isEOL() {
    return type == EOL;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isSequence()
   */
  public boolean isSequence() {
    return type == SEQUENCE;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isNumeric()
   */
  public boolean isNumeric() {
    return type == NUMERIC;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isOperator()
   */
  public boolean isOperator() {
    return type == OPERATOR;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isQuotedText()
   */
  public boolean isQuotedText() {
    return type == QUOTED_TEXT;
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#isText()
   */
  public boolean isText() {
    return ( type == TEXT ) || ( type == QUOTED_TEXT );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.util.tokenizer.Token#lineNo()
   */
  public int lineNumber() {
    return lineNo;
  }

  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return content;
  }

}
