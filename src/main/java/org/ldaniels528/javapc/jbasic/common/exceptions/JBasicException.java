package org.ldaniels528.javapc.jbasic.common.exceptions;

import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException;

/**
 * JBasic Synax Error
 */
@SuppressWarnings("serial")
public class JBasicException extends IbmPcException {

  /**
   * Creates an instance of this exception using
   * the given message as the cause
   * @param message the given message/cause
   */
  public JBasicException( String message ) {
    super( message );
  }
  
  /**
   * Default constructor
   */	
  public JBasicException( String message, Throwable cause ) {
    super( message, cause );
  }

  /* (non-javadoc)
   * @see Exception#Exception(Exception)
   */
  public JBasicException( Throwable cause ) {
    super( cause );
  }

  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  return super.getMessage();
  }

}
