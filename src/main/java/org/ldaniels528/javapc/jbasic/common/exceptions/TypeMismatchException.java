package org.ldaniels528.javapc.jbasic.common.exceptions;

/**
 * Exception to type mismatch errors
 */
@SuppressWarnings("serial")
public class TypeMismatchException extends RuntimeException {
  private final Object value;
  
  /**
   * Creates an instance of this exception
   * @param value the value that caused this exception
   */
  public TypeMismatchException( Object value ) {
	  super( "Type mismatch" );
	  this.value = value;
  }
  
  public Object getValue() {
	  return value;
  } 


}
