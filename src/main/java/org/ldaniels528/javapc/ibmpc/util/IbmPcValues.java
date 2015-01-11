package org.ldaniels528.javapc.ibmpc.util;

import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcNumericFormatException;

/**
 * IBM PC Values Utility
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcValues {
	  // define the hexadecimal characters
	  private static final char[] HEXADECIMAL_DIGITS = "0123456789ABCDEF".toCharArray();
	  // define the octal characters
	  private static final char[] OCTAL_DIGITS = "01234567".toCharArray();
	  // define the binary characters
	  private static final char[] BINARY_DIGITS = "01".toCharArray();	  	  
	  // define the binary characters
	  protected static final String QUOTE = "\"";
	  
	  ////////////////////////////////////////////////////////////
	  //      Constructor(s)
	  ////////////////////////////////////////////////////////////
		
	  /**
	   * No instantiation allowed
	   */
	  protected IbmPcValues() { }
	  
	  ////////////////////////////////////////////////////////////
	  //      Numeric to String Conversion Method(s)
	  ////////////////////////////////////////////////////////////
	  
	  /**
	   * Converts the given number to a binary string
	   * @param number the given number
	   * @return a binary string
	   */
	  public static String toBinary( final int number ) {
		  return toBaseN( BINARY_DIGITS, number );
	  }
	  
	  /**
	   * Converts the given number to a hexadecimal string
	   * @param number the given number
	   * @return a hexadecimal string
	   */
	  public static String toHexadecimal( final int number ) {
		  return toBaseN( HEXADECIMAL_DIGITS, number );
	  }
	  
	  /**
	   * Converts the given number to a octal string
	   * @param number the given number
	   * @return a octal string
	   */
	  public static String toOctal( final int number ) {
		  return toBaseN( OCTAL_DIGITS, number );
	  }
	  
	  /**
	   * Converts the given number to a Base<supercript>N</superscript> string
	   * @param baseNdigits the characters that represents the Base<supercript>N</superscript>
	   * digits. (For example, binary would be <code>new char[] { '0', '1' }</code> 
	   * @param number the given number
	   * @return a Base<supercript>N</superscript> string
	   */
	  public static String toBaseN( final char[] baseNdigits, final int number ) {
		  // define the actual base number
		  final int baseN = baseNdigits.length;
		  
		  // define our starting value
		  int remaining = number;
		  
		  // generate the baseN number (in reverse order)
		  final StringBuilder sb = new StringBuilder();
		  while( remaining > 0 ) {
			  // determine the next digit
			  final int digit = remaining % baseN;		  
			  		  
			  // append the digit to the string
			  sb.append( baseNdigits[ digit ] );
			  
			  // reduce the remaining value
			  remaining /= baseN;
		  }	  
		  // return a reversed copy of the string
		  return sb.reverse().toString();
	  }
	
	  ////////////////////////////////////////////////////////////
	  //      String to Numeric Conversion Method(s)
	  ////////////////////////////////////////////////////////////
	  
	  /**
	   * Parses a baseN number
	   * @param baseNdigits the valid digits for the given baseN (i.e. octal is "01234567" )
	   * @param baseNString the baseN numeric string which is to be converted
	   * @return the decimal integer equivalent of the given baseN number string 
	   * @throws IbmPcNumericFormatException
	   */
	  public static int parseBaseN( final String baseNdigits, String baseNString ) 
	  throws IbmPcNumericFormatException {
		  final int base = baseNdigits.length();
		  final char[] baseNarray = baseNString.toCharArray();
		  
		  // compute the value
		  int total = 0;
		  int factor = 1;
		  for( int n = baseNarray.length-1; n >= 0; n-- ) {
			  // get the octal digit
			  final int digit = baseNdigits.indexOf( String.valueOf( baseNarray[n] ) );
			  
			  // validate the digit
			  if( digit < 0 || digit >= base )
				  throw new IbmPcNumericFormatException( baseNString );
			  					
			  // compute the value for this hexadecimal place
			  total += digit * factor;
			  factor *= base;
		  }
		  
		  return total;
	  }
	  
	  /**
	   * Parses the given hexadecimal string into an integer value 
	   * @param hexString the given hexadecimal string
	   * @return the integer equivalent value 
	   * @throws IbmPcNumericFormatException
	   */
	  public static int parseHexadecimalString( final String hexString ) 
	  throws IbmPcNumericFormatException {
		  final String HEX_DIGITS = "0123456789ABCDEF";
		  						
		  // return the integer equivalent of the hexadecimal string
		  return parseBaseN( HEX_DIGITS, hexString );
	  }

}
