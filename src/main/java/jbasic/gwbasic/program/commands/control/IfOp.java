package jbasic.gwbasic.program.commands.control;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.OpCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Expression;
import jbasic.common.values.Value;
import jbasic.common.values.impl.SimpleExpression;
import jbasic.common.values.impl.SimpleExpressionSet;
import jbasic.gwbasic.program.GwBasicCompiler;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * IF Command
 * <br>Syntax: IF <i>expression</i> THEN <i>statement1</i> [ELSE <i>statement2</i>]
 * <br>Example: IF X > 5 THEN PRINT "YES" ELSE PRINT "NO"
 */
public class IfOp extends GwBasicCommand {
	private static final String THEN_KEYWORD = "THEN";  
	private static final String ELSE_KEYWORD = "ELSE";
	private static final String AND_KEYWORD  = "AND";
	private static final String OR_KEYWORD	   = "OR";
	private Collection<Expression> expressions;
	private OpCode thenOpCode;
	private OpCode elseOpCode;

  /**
   * Creates an instance of this opCode
 * @param it the parsed text that describes the BASICA/GWBASIC instruction
   * @throws JBasicException
   */
  public IfOp( final TokenIterator it ) 
  throws JBasicException {
	  parse( it );
  }

  /**
   * Executes this {@link jbasic.common.program.OpCode opCode}
   * @param compiledCode the currently running {@link JBasicCompiledCode compiled code}
   * @throws JBasicException
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
    // evaluate the expression, if affirmative execute the "Then" opCode
    // otherwise, execute the "Else" opCode
    if( evaluate( compiledCode ) ) {
    		thenOpCode.execute( compiledCode );
    }
    else if( elseOpCode != null ) {
    		elseOpCode.execute( compiledCode );
    }
  }

  /**
   * Evaluates the expression
   * @param compiledCode the currently running {@link JBasicCompiledCode compiled code}
   * @return true, if the comparison results in a true condition
   * @throws JBasicException
   */
  private boolean evaluate( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
    for( final Iterator<Expression> it = expressions.iterator(); it.hasNext(); ) {
    		final Expression expression = it.next();
    		if( expression.evaluate( compiledCode ) ) {
    			return true;
    		}
    }
    return false;
  }

  /**
   * Retrieves the OpCode for a Then or Else statement
   * @param it the given {@link TokenIterator token iterator}
   * @return an {@link OpCode opCode}
   * @throws JBasicException
   */
  private OpCode getOpCode( final TokenIterator it ) 
  throws JBasicException {
	  return GwBasicValues.isNumericConstant( it.peekAtNext() )
        	? new GotoOp( it )
        	: GwBasicCompiler.getInstance().compile( it );
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime.
 * @param it the given {@link TokenIterator iterator}
   * @throws jbasic.common.exceptions.JBasicException
   */
  private void parse( final TokenIterator it ) 
  throws JBasicException {
	  // get the expressions ...
	  expressions = parseExpressions( it );
    		  
      // get statement to execute
      thenOpCode = getOpCode( it.upto( ELSE_KEYWORD, true ) );

      // is there more to parse?
      if( it.hasNext() ) {    	  
        // next token should be 'ELSE' (conditional)
    	  	JBasicTokenUtil.mandateToken( it, ELSE_KEYWORD );

        // get else statement
        elseOpCode = getOpCode( it );

        // there there are more elements; syntax error
        if( it.hasNext() ) throw new SyntaxErrorException();      
    }
  }
  
  /**
   * Parses an expression from the given token iterator
   * @param it the given {@link TokenIterator token iterator}
   * @return a new {@link SimpleExpression expression}
   * @throws JBasicException
   */
  private Expression parseExpression( final TokenIterator it ) 
  throws JBasicException {
      // get the 1st value
      final Value value1 = GwBasicValues.getValueReference( it );

      // get the comparator
      final String comparator = it.next();

      // get the 2nd value
      final Value value2 = GwBasicValues.getValueReference( it );
      
      // return a new expression
	  return new SimpleExpression( value1, comparator, value2 );
  }
  
  /**
   * Parses an expression from the given token iterator
   * @param it the given {@link TokenIterator token iterator}
   * @return a new {@link Collection collection} of {@link Expression expressions}
   * @throws JBasicException
   */
  private Collection<Expression> parseExpressions( final TokenIterator it ) 
  throws JBasicException {
	  // create a container for all expressions
	  final Collection<Expression> expressions = new LinkedList<Expression>();
	    
	  // create an initial container for this set of expression(s)
	  SimpleExpressionSet set = new SimpleExpressionSet();
	  expressions.add( set );
	  
	  // get the next expression
	  boolean quit = false;
	  do {
    	  // get at least one expression
    	  final Expression expression = parseExpression( it );
    	  
    	  // add it to the collection of expressions
    	  set.add( expression );	    	  
    	  
    	  // get the next keyword
    	  final String nextKeyWord = it.next();
    	  
    	  // if the next keyword is 'THEN' ...
    	  if( THEN_KEYWORD.equalsIgnoreCase( nextKeyWord ) ) {
    		  quit = true;
    	  }
    	  
    	  // if the next keyword is 'AND' ...
    	  else if( AND_KEYWORD.equalsIgnoreCase( nextKeyWord ) ) {
    		  // do nothing here
    	  }
    	  
    	  // if the next keyword is 'OR' ...
    	  else if( OR_KEYWORD.equalsIgnoreCase( nextKeyWord ) ) {
    		  // create a new container for the next set of expression(s)
    		  set = new SimpleExpressionSet();
    		  expressions.add( set );
    	  }
    	  
    	  // dunno what it was ...
    	  else {
    		  throw new JBasicException( "Expected keyword AND, OR, or THEN; found '" + nextKeyWord + "'" );
    	  }
	  } 
	  while( !quit );
	  
	  // return the collection of expressions
	  return expressions;
  }

}
