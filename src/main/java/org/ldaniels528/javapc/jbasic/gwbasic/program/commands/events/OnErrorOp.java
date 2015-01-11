package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.events;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NotYetImplementedException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
   * @see org.ldaniels528.javapc.jbasic.common.program.OpCode#execute(org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode)
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {
	  //compiledCode.gotoLabel( label, false );
	  
	  // TODO Setup a way to trap exceptions
	  throw new NotYetImplementedException();
  }

}
