package jbasic.gwbasic.values;

import ibmpc.devices.memory.NumberMemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import ibmpc.exceptions.IbmPcNumericFormatException;
import jbasic.common.exceptions.IllegalNumberFormat;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.common.values.Values;
import jbasic.common.values.VariableReference;
import jbasic.common.values.impl.SimpleConstant;
import jbasic.common.values.impl.SimpleVariableArrayIndexReference;
import jbasic.common.values.impl.SimpleVariableReference;
import jbasic.common.values.types.impl.JBasicTempNumber;
import jbasic.common.values.types.impl.JBasicTempString;
import jbasic.gwbasic.functions.GwBasicFunctionManager;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * Utility class for managing {@link Value value implementations}
 */
public class GwBasicValues extends Values {
  // create a single instance of the function manager
  private static final GwBasicFunctionManager functionManager = new GwBasicFunctionManager();  
	
  ////////////////////////////////////////////////////////////
  //      Constructor(s)
  ////////////////////////////////////////////////////////////
	
  /**
   * No instantiation allowed
   */
  protected GwBasicValues() { }

  ////////////////////////////////////////////////////////////
  //      Value Method(s)
  ////////////////////////////////////////////////////////////
  
  /**
   * Retrieves the a string or numeric constant only
   * @param it the given {@link TokenIterator iterator}
   * @return a {@link jbasic.common.values.VariableReference reference to the variable}
   * @throws jbasic.common.exceptions.JBasicException
   */
  public static SimpleConstant getConstantValue( TokenIterator it ) 
  throws JBasicException {
	  // get the value
	  Value value = getValue( it );
	  
	  // the value must be a constant
	  if( !( value instanceof SimpleConstant ) ) 
		  throw new SyntaxErrorException();
	  
	  return (SimpleConstant)value;
  }

  /**
   * Retrieves the reference to a variable or value
   * @param it the given {@link TokenIterator iterator}
   * @return a {@link jbasic.common.values.VariableReference reference to the variable}
   * @throws jbasic.common.exceptions.JBasicException
   */
  public static Value getValue( TokenIterator it ) 
  throws JBasicException {	
	// peek at the next token
	final String token = JBasicTokenUtil.nextToken( it );

    // is it a numeric constant?
    if( isNumericConstant( token ) ) {
    		final NumberMemoryObject number = parseNumber( token );    		
      	return new SimpleConstant( number );
    }
    
    // is it a negative numeric constant?
    else if( token.equals( "-" ) && 
    		it.hasNext() && isNumericConstant( it.peekAtNext() ) ) {
    		final String negNumber = token + it.next();
    		final NumberMemoryObject number = parseNumber( negNumber );	
    		return new SimpleConstant( number );
    }
    
    // is it a string constant?
    else if( isStringConstant( token ) ) {
    		final StringMemoryObject string = new JBasicTempString();
    		string.setContent( token.substring( 1, token.length() - 1 ) );
    		return new SimpleConstant( string );
    }
    
    // is it a sub-block (i.e. "(x+3)")?
    else if( token.equals( "(" ) ) {
    		// get the inner values
    		Value value = new GwBasicCompositeValue( it );    	
    		// require closing parenthesis
    		mandateToken( it, ")" );
    		// return the value
    		return value;
    }
    
    // is it a function?
    else if( functionManager.functionExists( token ) ) {
    		return functionManager.callFunction( token, it );
    }
    
    // if the next token is a '(', then it may be a variable array
    else if( it.hasNext() && "(".equals( it.peekAtNext() ) ) {
    		// get the value or variable name
    		final String varName = token;
    		// skip the opening parenthesis
    		mandateToken( it, "(" );
    		// get the array index
    		final Value index = new GwBasicCompositeValue( it );
    		// must have a closing parenthesis
    		mandateToken( it, ")" );
    		// return the array index reference
    		return new SimpleVariableArrayIndexReference( varName, index );
    }

    // may be a variable
    else return new SimpleVariableReference( token );
  }
  
  /**
   * Retrieves the reference to a variable or value
   * @param it the given {@link TokenIterator iterator}
   * @return a {@link jbasic.common.values.VariableReference reference to the variable}
   * @throws jbasic.common.exceptions.JBasicException
   */
  public static Value getValueReference( TokenIterator it )
  throws JBasicException {
	  return new GwBasicCompositeValue( it );
  }
  
  /**
   * Retrieves the reference to a variable only
   * @param it the given {@link TokenIterator token iterator}
   * @return a {@link jbasic.common.values.VariableReference reference} to the variable
   * @throws jbasic.common.exceptions.JBasicException
   */
  public static VariableReference getVariableReference( TokenIterator it )
  throws JBasicException {
	  // get a value
	  final Value value = getValue( it );
	  
	  // if the value is not a variable, type mismatch
	  if( !( value instanceof VariableReference ) ) 
		  throw new SyntaxErrorException();
	  
	  // return the variable
	  return (VariableReference)value;
  }
  
  ////////////////////////////////////////////////////////////
  //      Data Conversion Method(s)
  ////////////////////////////////////////////////////////////    
  
  /**
   * @return the unsigned value of the given integer
   */
  public static int getUnsignedInt( final int value ) {
	  return ( value < 0 ) ? value + 65536 : value;	  
  }
  
  /** 
   * Parses the given numeric string into a {@link NumberMemoryObject number object}
   * @param number the given numeric string
   */
  public static NumberMemoryObject parseNumber( String number ) 
  throws JBasicException {
    // is it an integer constant?
    if( isIntegerConstant( number ) ) {
    		final int value = parseIntegerString( number );
    		final NumberMemoryObject integer = new JBasicTempNumber( value );
      	return integer;
    }
	    
    // is it a decimal constant?
    else if( isDecimalConstant( number ) ) {
    		final double value = parseDecimalString( number );
    		final NumberMemoryObject decimal = new JBasicTempNumber( value );
      	return decimal;
    }
    
    // is it a hexadecimal constant?
    else if( isHexadecimalConstant( number ) ) {
		final int value = parseHexadecimalString( number );
		final NumberMemoryObject integer = new JBasicTempNumber( value );
		return integer;
    }    
    
    // is it a octal constant?
    else if( isOctalConstant( number ) ) {
		final int value = parseOctalString( number );
		final NumberMemoryObject integer = new JBasicTempNumber( value );
		return integer;
    }	  
    
    // shouldn't happen
    else throw new IllegalNumberFormat( number );
  }
  
  /**
   * Parses the given decimal string into a decimal value 
   * @param decString the given decimal string
   * @return the decimal equivalent value 
   * @throws IllegalNumberFormat
   */
  public static double parseDecimalString( String decString ) 
  throws IllegalNumberFormat {
	  try {
		  return Double.parseDouble( decString );
	  }
	  catch( NumberFormatException e ) {
		  throw new IllegalNumberFormat( decString );
	  }
  }
  
  /**
   * Parses the given hexadecimal string into an integer value 
   * @param hexString the given hexadecimal string
   * @return the integer equivalent value 
   * @throws IllegalNumberFormat 
   */
  public static int parseHexadecimalString( String hexString ) 
  throws IllegalNumberFormat {
	  final String HEX_DIGITS = "0123456789ABCDEF";
	  
	  // convert the string into characters
	  final String hexdata = hexString.startsWith( "&H" ) 
	  						? hexString.substring( 2 ) 
	  					    : hexString;
	  						
	  // return the integer equivalent of the hexadecimal string
	  try {
		  return parseBaseN( HEX_DIGITS, hexdata );
	  }
	  catch( final IbmPcNumericFormatException e ) {
		  throw new IllegalNumberFormat( e.getNumericString() );
	  } 
  }
  
  /**
   * Parses the given integer string into a integer value 
   * @param intString the given integer string
   * @return the integer equivalent value 
   * @throws IllegalNumberFormat 
   */
  public static int parseIntegerString( String intString ) 
  throws IllegalNumberFormat {
	  final String DECIMAL_DIGITS = "0123456789";
	  final String NUMBER_ENDINGS = "#%!";
	  
	  // is the number negative?
	  final boolean isNegative = intString.startsWith( "-" ); 
	  if( isNegative ) {
		  intString = intString.substring( 1 );
	  }
	  
	  // does the number have a type indicator?
	  final int ending = NUMBER_ENDINGS.indexOf( intString.charAt( intString.length() - 1 ) );
	  if( ending != -1 ) {
		  intString = intString.substring( 0, intString.length() - 1 );
	  }
	  	  			
	  try {
		  // return the integer equivalent of the octal string	  		
		  int number = parseBaseN( DECIMAL_DIGITS, intString );
		  return isNegative ? -number : number;
	  }
	  catch( final IbmPcNumericFormatException e ) {
		  throw new IllegalNumberFormat( e.getNumericString() );
	  }
  }
  
  /**
   * Parses the given octal string into an integer value 
   * @param hexString the given octal string
   * @return the integer equivalent value 
   * @throws IllegalNumberFormat 
   */
  public static int parseOctalString( String octalString ) 
  throws IllegalNumberFormat {
	  final String OCTAL_DIGITS = "01234567";
	  
	  // convert the string into characters
	  final String octdata = octalString.startsWith( "&O" ) 
	  						? octalString.substring( 2 ) 
	  					    : ( octalString.startsWith( "&" ) 
	  					    		? octalString.substring( 1 ) 
                                  : octalString );

	  try {
		  // return the integer equivalent of the octal string	  		
		  return parseBaseN( OCTAL_DIGITS, octdata );
	  }
	  catch( final IbmPcNumericFormatException e ) {
		  throw new IllegalNumberFormat( e.getNumericString() );
	  }
  }
  
  /**
   * Parses the given numeric string into an decimal value 
   * @param hexString the given numeric string
   * @return the decimal equivalent value 
   * @throws IllegalNumberFormat 
   */
  public static double parseNumericString( String numString ) 
  throws IllegalNumberFormat {
	  // decimal string?
	  if( isDecimalConstant( numString  ) )
		  return parseDecimalString( numString );
	  // hexadecimal string?
	  else if( isHexadecimalConstant( numString  ) )
		  return parseHexadecimalString( numString );
	  // integer string?
	  else if( isIntegerConstant( numString  ) )
		  return parseIntegerString( numString );
	  // octal string?
	  else if( isOctalConstant( numString  ) )
		  return parseHexadecimalString( numString );
	  // unknown ...
	  else
		  throw new IllegalNumberFormat( numString );
  }
  
  ////////////////////////////////////////////////////////////
  //      Numeric Conversion Method(s)
  ////////////////////////////////////////////////////////////

	/**
	 * Returns the given number as an unsigned integer value
	 * @param number the given number
	 * @return an unsigned integer value
	 */
	public static int toUnsignedInteger( int number ) {
		return ( number < 1 ) ? number + 65536 : number;
	}
  
	  ////////////////////////////////////////////////////////////
	  //      Data Type Testing Method(s)
	  ////////////////////////////////////////////////////////////

	  /**
	   * Validates whether the given content is a decimal constant
	   * @param content the given content
	   * @return true, if the content is a valid number (i.e. 345.6)
	   */
	  public static boolean isDecimalConstant( String content ) {
		  return content.matches( "-?\\d*?\\.\\d+" ); 
	  }
	  
	  /**
	   * Validates whether the given content is an hexadecimal constant
	   * @param content the given content
	   * @return true, if the content is a valid hexadecimal number (i.e. <tt>&HB80A</tt>)
	   */
	  public static boolean isHexadecimalConstant( String content ) {
		  return content.matches( "&H[0-9,A-F,a-f]+" ); 
	  }
	  
	  /**
	   * Validates whether the given content is an integer constant
	   * @param content the given content
	   * @return true, if the content is a valid number (i.e. <tt>34567</tt>)
	   */
	  public static boolean isIntegerConstant( String content ) {
		  return content.matches( "-?\\d+" );
	  }
	  
	  /**
	   * Validates whether the given content is a numeric constant
	   * @param content the given content
	   * @return true, if the content is a valid number (i.e. <tt>345.67</tt>)
	   */
	  public static boolean isNumericConstant( String content ) {
		  return isDecimalConstant( content ) || 	  			
		  			isHexadecimalConstant( content ) || 
		  			isIntegerConstant( content ) ||
		  			isOctalConstant( content );
	  }
	  
	  /**
	   * Validates whether the given content is an octal constant
	   * @param content the given content
	   * @return true, if the content is a valid octal number (i.e. <tt>&O34567</tt>)
	   */
	  public static boolean isOctalConstant( String content ) {
		  return content.matches( "&O?[0-7]+" ); 
	  }

  /**
   * Validates whether the given content is a string constant
   * @param content the given content
   * @return true, if the content is double quoted (i.e. "Hello")
   */
  public static boolean isStringConstant( String content ) {
	  return content.startsWith( QUOTE ) && content.endsWith( QUOTE );
  }
    
}