package jbasic.gwbasic.program.commands.console;

import ibmpc.devices.display.IbmPcDisplay;

import java.util.LinkedList;
import java.util.List;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * PRINT Command
 * <br>Purpose: To output a display to the screen.
 * <br>PRINT [<i>list of expressions</i>][;]
 * <br>Example: PRINT "My name is ", $NAME
 */
public class PrintOp extends GwBasicCommand {  
  private final StringBuilder buffer;
  private final Value[] values;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public PrintOp( TokenIterator it ) throws JBasicException {	  
	  this.buffer = new StringBuilder( 255 );
	  this.values = parse( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get an instance of the screen
	  final IbmPcDisplay screen = program.getSystem().getDisplay();
	  
	  // clear the buffer
	  buffer.delete( 0, buffer.length() );
	  
	  // copy all the values to the buffer
	  for( Value value : values ) {
		  buffer.append( value.getValue( program ).toString() );
	  }
	  
	  // display the contents of the buffer
	  screen.writeLine( buffer.toString() );
	  screen.update();
  }

  /**
   * Converts the given textual representation into {@link Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  private Value[] parse( TokenIterator it ) throws JBasicException {
	  List<Value> values = new LinkedList<Value>();
	  while( it.hasNext() ) {
		  // add the next value
		  values.add( GwBasicValues.getValueReference( it ) );

		  // if there are more tokens, it should be a semicolon
		  if( it.hasNext() ) {
			  if( it.peekAtNext().equals( "," ) || 
				  it.peekAtNext().equals( ";" ) ) it.next();
			  else
				  throw new SyntaxErrorException();        
		  }
	  }
	  return values.toArray( new Value[ values.size() ] );
  }

  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	return "PRINT";  
  }

}
