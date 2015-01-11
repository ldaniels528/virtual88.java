package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.events;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * ON KEY(n) GOSUB Command
 * <br>Syntax: ON KEY(<i>keyCode</i>) GOSUB <i>label</i>
 */
public class OnKeyOp extends GwBasicCommand {
	  private final Value keyCode;
	  private final String label;

	  /**
	   * Creates an instance of this operation
	   * @param it the given {@link TokenIterator token iterator}
	   * @throws JBasicException
	   */
	  public OnKeyOp( final TokenIterator it ) throws JBasicException {
		  // extract just the tokens we need
		  final TokenIterator args = JBasicTokenUtil.extractTokens( it, "KEY", "(", "@@", ")", "GOSUB", "@@" );
		  
		  // get the key code value
		  this.keyCode = GwBasicValues.getValue( args );
		  
		  // get the line number
		  this.label = JBasicTokenUtil.nextToken( args );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // test the key code: must be numeric
		  final MemoryObject keyCodeObj = keyCode.getValue( compiledCode );
		  if( keyCodeObj.isNumeric() ) {
			  throw new TypeMismatchException( keyCodeObj );
		  }
		  
		  // call the subroutine
		  compiledCode.gotoLabel( label, true );
	  }

}
