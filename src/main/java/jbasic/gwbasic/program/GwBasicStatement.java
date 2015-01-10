package jbasic.gwbasic.program;

import jbasic.common.program.JBasicProgramStatement;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * Represents a BASICA/GWBASIC Program Statement
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicStatement implements JBasicProgramStatement {	
	private static final int MIN_LINE_NUMBER 	= 0;
	private static final int MAX_LINE_NUMBER 	= 65535;
	private final Integer lineNumber;
	private String code;

	  //////////////////////////////////////////////////////
	  //    Constructor(s)
	  //////////////////////////////////////////////////////
	  
	  /**
	   * Creates a new statement on the given line number
	   * @param lineNumber the line number that the statement will occupy
	   * @param code the code that will be found at the given line number
	   */
	  public GwBasicStatement( final int lineNumber, final String code ) {
	    this.lineNumber = lineNumber;
	    this.code 		= ( code != null ) ? toUpperCase( code ) : code;
	  }
	  
	  /**
	   * Creates a new statement (without a line number)
	   * @param code the GWBASIC codew that is to be executed
	   */
	  public GwBasicStatement(  final String code ) {
		  this( 0, code );
	  }

	  //////////////////////////////////////////////////////
	  //    Service Method(s)
	  //////////////////////////////////////////////////////
	  
	  /* 
	   * (non-Javadoc)
	   * @see jbasic.program.JBasicStatement#getLineNumber()
	   */
	  public Integer getLineNumber() {
	    return lineNumber;
	  }
	
	  /* 
	   * (non-Javadoc)
	   * @see jbasic.program.JBasicStatement#getCode()
	   */
	  public String getCode() {
	    return code;
	  }
	  
	  /* 
	   * (non-Javadoc)
	   * @see jbasic.program.JBasicStatement#setCode(java.lang.String)
	   */
	  public void setCode( String code ) {
		  this.code = code;
	  }
	
	  /* 
	   * (non-Javadoc)
	   * @see java.lang.Object#equals(java.lang.Object)
	   */
	  public boolean equals( Object o ) {
	    return ( o.hashCode() == this.hashCode() );
	  }
	
	  /* 
	   * (non-Javadoc)
	   * @see java.lang.Object#hashCode()
	   */
	  public int hashCode() {
	    return lineNumber.hashCode();
	  }
	
	  /* 
	   * (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	  public String toString() {
	    return new StringBuilder( 6 + code.length() ).append( lineNumber ).append( ' ' ).append( code ).toString();
	  }
	  
	  //////////////////////////////////////////////////////
	  //    Internal Service Method(s)
	  //////////////////////////////////////////////////////
	  
	  /**
	   * Changes the case of the given code string;
	   * Changing all non-quoted characters from lowercase
	   * to uppercase.
	   * @param code the given code string
	   * @return an uppercase string version of the code string
	   */
	  private String toUpperCase( String code ) {		  
		  // break the code string into a character array
		  final char[] chardata = code.toCharArray();
		  
		  // capitalize non-quote characters
		  boolean quoted = false;
		  for( int n = 0; n < chardata.length; n++ ) {
			  final char c = chardata[n];
			  
			  // if we encounter a quote, flip the flag
			  if( c == '"' ) quoted = !quoted;
			  
			  // if 'a' thru 'z' and not quoted, capitalize it
			  if( c >= 'a' && c <= 'z' && !quoted )
				  chardata[n] = (char)( c & 0xDF );
		  }
		  
		  return String.valueOf( chardata );
	  }
	  
	  /* 
	   * (non-Javadoc)
	   * @see ibmpc.program.IbmPcProgram#isValidLabel(java.lang.String)
	   */
	  public boolean validateLabel( String label ) {
		  // the label must be numeric
		  if( !GwBasicValues.isNumericConstant( label ) ) return false;
		  
		  // check the range of the line number
		  final int lineNumber = (int)GwBasicValues.parseNumericString( label );
		  return ( lineNumber >= MIN_LINE_NUMBER ) && ( lineNumber <= MAX_LINE_NUMBER );   
	  }
	  
}
