package org.ldaniels528.javapc.jbasic.gwbasic.program.commands;

import org.ldaniels528.javapc.jbasic.common.program.OpCode;

/**
 * Abstract base class for all GWBASIC OpCodes
 */
public abstract class GwBasicCommand implements OpCode {
	private int lineNumber;
  
  ///////////////////////////////////////////////////////
  //      Line-number Accessor and Mutator Method(s)
  ///////////////////////////////////////////////////////

  /**
   * @return the line number that this opCode corresponds to in
   * the {@link org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode program}.
   */
  public int getLineNumber() {
	  return lineNumber;
  }

  /**
   * Sets the line number that this opCode corresponds to
   * in the {@link org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode program}.
   * @param lineNumber the given line number
   */
  public void setLineNumber( final int lineNumber ) {
	  this.lineNumber = lineNumber;
  }
  
  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  return getClass().getSimpleName();
  }
    
}
