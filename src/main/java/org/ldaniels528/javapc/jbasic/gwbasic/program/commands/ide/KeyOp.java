package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  final GwBasicEnvironment gwBasicEnvironment = (GwBasicEnvironment)program.getSystem();
	  gwBasicEnvironment.keyLabelsDisplayed( keysDisplayed );
  }

  /**
   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
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
