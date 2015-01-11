package jbasic.gwbasic.program.commands.control;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.AbortableOpCode;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * End Program Command (END)
 */
public class EndOp extends GwBasicCommand implements AbortableOpCode {

  /**
   * Default constructor
   * @param it the given {@link TokenIterator token iterator}
   */
  public EndOp( final TokenIterator it ) {
	  super();
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
	  // do nothing
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.AbortableOpCode#abort()
   */
  public boolean abort() {
	  return true;
  }

}
