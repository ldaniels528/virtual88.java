package jbasic.gwbasic.program.commands.events;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NotYetImplementedException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * ON ERROR Command
 * <br><b>Purpose</b>: To branch to one of several specified line numbers, 
 * depending on the value returned when an expression is evaluated.
 * <br><b>Syntax1</b>: ON ERROR GOTO <i>line number</i>
 */
public class OnErrorOp extends GwBasicCommand {	
	private final String label;

  /**
   * Default Constructor
   * @param it the given {@link TokenIterator token iterator}
   * @throws JBasicException
   */
  public OnErrorOp( final TokenIterator it ) throws JBasicException {  
	  // expect 'ERROR'
	  JBasicTokenUtil.mandateToken( it, "ERROR" );
	  
	  // expect 'GOTO'
	  JBasicTokenUtil.mandateToken( it, "GOTO" );
	  
	  // parse the label/line number
	  this.label = JBasicTokenUtil.nextToken( it );
	  
	  // no more tokens
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.common.program.OpCode#execute(jbasic.common.program.JBasicSourceCode)
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  //compiledCode.gotoLabel( label, false );
	  
	  // TODO Setup a way to trap exceptions
	  throw new NotYetImplementedException();
  }

}
