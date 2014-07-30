package jbasic.common.program;

/**
 * JBasic Language Statement
 */
public interface JBasicProgramStatement {
	
  /**
   * @return the line number of this statement
   */
  public Integer getLineNumber();

  /**
   * @return the code that represents a single line in the program
   */
  public String getCode();
  
  /**
   * Sets the BASIC code in this statement
   * @param code the given BASIC code
   */
  public void setCode( String code );

}
