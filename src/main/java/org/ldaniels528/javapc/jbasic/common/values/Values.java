package org.ldaniels528.javapc.jbasic.common.values;

import org.ldaniels528.javapc.ibmpc.util.IbmPcValues;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.exceptions.UnexpectedTokenException;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * Utility class for managing {@link Value value implementations}
 */
public class Values extends IbmPcValues {
	  
	  ////////////////////////////////////////////////////////////
	  //      Utility Method(s)
	  ////////////////////////////////////////////////////////////

	  /**
	   * Mandates that the next token matches the supplied expected token
	   * @param it the given {@link TokenIterator iterator}
	   * @param expected the expected token
	   * @throws JBasicException
	   */
	  public static void mandateToken( TokenIterator it, String expected )
	  throws JBasicException {
	  		final String found = nextValue( it );
	  		if( !found.equals( expected ) )
	  			throw new UnexpectedTokenException( expected, found );
	  }

	  /**
	   * Returns the next string value from the iterator
	   * @param it the given {@link java.util.Iterator iterator}
	   * @return the next string value in the iterator
	   * @throws JBasicException if there is not a next element
	   */
	  public static String nextValue( TokenIterator it ) 
	  throws JBasicException {
	  		if( !it.hasNext() ) throw new SyntaxErrorException();
	  		else return it.next();
	  }
	  
	  /**
	   * Insures that there are no more tokens
	   * @param it the given {@link java.util.Iterator iterator}
	   * @throws JBasicException
	   */
	  public static void noMoreTokens( TokenIterator it )
	  throws JBasicException  {
		  if( it.hasNext() ) throw new SyntaxErrorException();
	  }
	  
}
