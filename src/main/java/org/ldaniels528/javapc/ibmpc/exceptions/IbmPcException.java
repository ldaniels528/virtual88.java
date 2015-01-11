package org.ldaniels528.javapc.ibmpc.exceptions;

/**
 * IBM PC Base Exception Class
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class IbmPcException extends Exception {

	  /**
	   * Creates an instance of this exception using
	   * the given message as the cause
	   * @param message the given message/cause
	   */
	  public IbmPcException( String message ) {
	    super( message );
	  }
	  
	  /**
	   * Default constructor
	   */	
	  public IbmPcException( String message, Throwable cause ) {
	    super( message, cause );
	  }

	  /* (non-javadoc)
	   * @see Exception#Exception(Exception)
	   */
	  public IbmPcException( Throwable cause ) {
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
