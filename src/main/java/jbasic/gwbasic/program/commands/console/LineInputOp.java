package jbasic.gwbasic.program.commands.console;

import jbasic.common.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.values.Value;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableReference;
import jbasic.common.values.impl.SimpleVariableReference;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * LINE INPUT Instruction
 * Syntax: LINE INPUT <var$>
 * Example: LINE INPUT NAME$
 */
public class LineInputOp extends GwBasicCommand {
	  private VariableReference reference;
	  private Value output;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public LineInputOp( TokenIterator it ) throws JBasicException {
	    parse( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // get the screen instance
		  final IbmPcDisplay screen = program.getSystem().getDisplay();
		  
	      // if output text was specified, display it
	      if( output != null ) {
	    	  	screen.write( output.getValue( program ).toString() );
	    	  	screen.update();
	      }

	      // lookup the variable
	      final Variable variable = reference.getVariable( program );

	      // get a scanner to read input
	      final IbmPcKeyboard scanner = program.getSystem().getKeyboard();
	      
	      // get a line of input
	      final String input = scanner.nextLine( );

	      // put the content into the variable
	      final MemoryObject varContent = variable.getValue( program );
	      varContent.setContent( input );
	  }

	  /**
	   * Converts the given textual representation into {@link jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link java.util.Iterator iterator}
	   * @throws jbasic.common.exceptions.JBasicException
	   */
	  private void parse( TokenIterator it ) throws JBasicException {
		if( !it.hasNext() ) throw new JBasicException( "Keyword 'INPUT' expected" );
		it.next();  
	    try {
	      while( it.hasNext() ) {
	        // look for output text
	        if( GwBasicValues.isStringConstant( it.peekAtNext() ) ) {
	          output = GwBasicValues.getValue( it );
	        }

	        // must be a variable or numeric value
	        else {
	          reference = new SimpleVariableReference( it.next() );
	        }

	        // there there are more elements; check for a command separator (semicolon)
	        if( it.hasNext() ) {
	          if( !it.peekAtNext().equals( ";" ) && !it.peekAtNext().equals( "," ) )
	            throw new JBasicException( it.next() );
	          else it.next();
	        }
	      }
	    }
	    catch( TypeMismatchException e ) {
	      throw new JBasicException( e );
	    }
	  }

	}
