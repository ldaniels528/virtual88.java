package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;


import java.util.Collection;
import java.util.LinkedList;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.common.values.impl.SimpleVariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * COMMON Command
 * @author lawrence.daniels@gmail.com
 */
public class CommonOp extends GwBasicCommand {
	private final Collection<VariableReference> references;
	  
	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public CommonOp( TokenIterator it ) throws JBasicException {
		  this.references = parse( it );
	  }

	  /**
	   * Executes this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  for( final VariableReference reference : references ) {
			  // TODO Do something here
			  throw new NotYetImplementedException();
		  }
	  }

	  /**
	   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator iterator}
	   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
	   */
	  private Collection<VariableReference> parse( TokenIterator it ) throws JBasicException {
		  Collection<VariableReference> arrays = new LinkedList<VariableReference>();
		  while( it.hasNext() ) {
		      // get the variable's name
		      final String varName = JBasicTokenUtil.nextToken( it );
		
			  // is the variable an array?
		      if( "(".equals( it.peekAtNext() ) ) {
		    	  	it.next();
		    	  	JBasicTokenUtil.mandateToken( it, ")" );
		    	  	arrays.add( new SimpleVariableReference( varName + "()" ) );
		      }
		      
		      // must be a normal variable
		      else {
		    	  	arrays.add( new SimpleVariableReference( varName ) );
		      }
		      
		      // if there's another element, it must be a comma
		      if( it.hasNext() ) {
		    	  	JBasicTokenUtil.mandateToken( it, "," );
		    	  	if( !it.hasNext() )
		    	  		throw new SyntaxErrorException();
		      }		      
		  }
		  return arrays;
	  }

}
