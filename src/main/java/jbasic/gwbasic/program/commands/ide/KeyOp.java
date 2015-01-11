package jbasic.gwbasic.program.commands.ide;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * Key Command
 * Syntax1: KEY ON|OFF
 */
public class KeyOp extends GwBasicCommand {
  private boolean keysDisplayed;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public KeyOp( TokenIterator it ) throws JBasicException {
	  parse( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  final GwBasicEnvironment gwBasicEnvironment = (GwBasicEnvironment)program.getSystem();
	  gwBasicEnvironment.keyLabelsDisplayed( keysDisplayed );
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  private void parse( TokenIterator it ) throws JBasicException {
    // KEY ON?
    if( it.peekAtNext().equals( "ON" ) ) {
      it.next();
      this.keysDisplayed = true;
    }

    // KEY OFF?
    else if( it.peekAtNext().equals( "OFF" ) ) {
      it.next();
      this.keysDisplayed = false;
    }

    // dunno what?
    else throw new SyntaxErrorException();

    // there should be no more tokens
    JBasicTokenUtil.noMoreTokens( it );
  }

}
