package jbasic.gwbasic.program.commands.console;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.system.IbmPcSystem;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.values.Value;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableReference;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * INPUT Instruction
 * <br><b>Syntax</b>: INPUT <i>variable</i>
 * <br><b>Example</b>: INPUT "What is your name?",NAME$
 */
public class InputOp extends GwBasicCommand {
  private VariableReference reference;
  private Value output;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public InputOp( TokenIterator it ) throws JBasicException {
	  parse( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.OpCode#execute(jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the display instance
	  final IbmPcSystem environment = program.getSystem();
	  final IbmPcDisplay display = environment.getDisplay(); 
	  
      // if output text was specified, display it
      if( output != null ) { 
    	  	display.write( output.getValue( program ).toString() );
    	  	display.update();
      }

      // lookup the variable
      final Variable variable = reference.getVariable( program );

      // get a scanner to read input
      final IbmPcKeyboard scanner = environment.getKeyboard();
      
      // get a line of input
      final String input = scanner.nextLine();
      
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
	  // there must be arguments
	  if( !it.hasNext() ) 
		  throw new SyntaxErrorException();
	  
	  // loop until we have reference (and maybe output)
	  while( it.hasNext() ) {      
		  // look for output text
		  if( GwBasicValues.isStringConstant( it.peekAtNext() ) ) {
			  output = GwBasicValues.getValue( it );
		  }
	
		  // must be a variable or numeric value
		  else {
			  reference = GwBasicValues.getVariableReference( it );
		  }
	
		  // there there are more elements; check for a command separator (semicolon)
		  if( it.hasNext() ) {
			  if( !it.peekAtNext().equals( ";" ) && !it.peekAtNext().equals( "," ) )
				  throw new JBasicException( it.next() );
			  else 
				  it.next();
		  }
	  }
  }

}
