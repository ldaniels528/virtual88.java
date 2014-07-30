package jbasic.gwbasic.program.commands.events;

import ibmpc.devices.memory.MemoryObject;

import java.util.LinkedList;
import java.util.List;

import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.VariableReference;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * ON <i>variable</i> Command
 * <br><b>Purpose</b>: To branch to one of several specified line numbers, 
 * depending on the value returned when an expression is evaluated.
 * <br><b>Syntax1</b>: ON <i>expression</i> GOTO <i>line numbers</i>
 * <br><b>Syntax2</b>: ON <i>expression</i> GOSUB <i>line numbers</i>
 */
public class OnOp extends GwBasicCommand {	
	private final List<String> labels;
	private final VariableReference reference;
	private final boolean subroutine;

  /**
   * Default Constructor
   * @param it the given {@link TokenIterator token iterator}
   * @throws JBasicException
   */
  public OnOp( TokenIterator it ) throws JBasicException {    
	  // get the variable referebce
	  this.reference	= GwBasicValues.getVariableReference( it );
	  
	  // determine whether GOTO or GOSUB is being used.
	  this.subroutine = parseGotoGosub( it );
	  
	  // parse the line numbers
	  this.labels = parseLabels( it );	  
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  // get the value of the variable
	  final MemoryObject value = reference.getValue( compiledCode );
	  if( !value.isNumeric() )
		  throw new TypeMismatchException( value );

	  // get the desired goto/gosub index
	  final int index = value.toInteger();
	  if( index < 0 || index > 255 )
		  throw new IllegalFunctionCallException( this );
	  
	  // determine which index to jump to
	  if( ( index > 0 ) && ( index <= labels.size() ) ) {
		  // get the label
		  final String label = labels.get( index  - 1 );
		  
		  // perform GOTO or GOSUB
		  compiledCode.gotoLabel( label, subroutine );
	  }
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  private List<String> parseLabels( TokenIterator it ) 
  throws JBasicException {
	  // create a container for returning the line numbers
	  List<String> labels = new LinkedList<String>();
	  
	  while( it.hasNext() ) {
		  // get the next line number
		  final String label = JBasicTokenUtil.nextToken( it );
		  		  
		  // add the line number to the list
		  labels.add( label );
		  
		  // if there's another token must be comma (,)
		  if( it.hasNext() ) JBasicTokenUtil.mandateToken( it, "," );
	  }
	  
	  // return the line numbers
	  return labels;
  }
  
  /**
   * Determine whether this statement is a subroutine (GOSUB) or not (GOTO)
   * @param it the given {@link TokenIterator iterator}
   * @return true, if GOSUB is used
   * @throws JBasicException
   */
  private boolean parseGotoGosub( TokenIterator it ) throws JBasicException {
	  // get the next token (the command)
	  final String command = JBasicTokenUtil.nextToken( it );
	  
	  // determine whether GOSUB or GOTO is used.
	  if( command.equals( "GOSUB " ) ) return true;
	  else if( command.equals( "GOTO " ) ) return false;
	  else throw new SyntaxErrorException();
  }

}
