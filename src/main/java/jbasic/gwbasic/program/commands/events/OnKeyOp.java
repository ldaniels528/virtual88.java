package jbasic.gwbasic.program.commands.events;

import ibmpc.devices.memory.MemoryObject;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

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
	   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
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
