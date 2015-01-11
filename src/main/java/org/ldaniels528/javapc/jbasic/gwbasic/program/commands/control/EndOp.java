package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.AbortableOpCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) throws JBasicException {
	  // do nothing
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.AbortableOpCode#abort()
   */
  public boolean abort() {
	  return true;
  }

}
