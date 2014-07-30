package jbasic.gwbasic.program.commands.console;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.system.IbmPcSystem;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * Clear Screen Command
 * Syntax: CLS
 */
public class ClearScreenOp extends GwBasicCommand {

  /**
   * Creates an instance of this {@link jbasic.common.program.OpCode opCode}
   * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator token iterator}
   * @throws JBasicException
   */
  public ClearScreenOp( TokenIterator it ) throws JBasicException {
	  JBasicTokenUtil.noMoreTokens( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  final IbmPcSystem environment = program.getSystem();
	  final IbmPcDisplay screen = environment.getDisplay();
	  screen.clear();
  }
  
  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  return "CLS";
  }

}