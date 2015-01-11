package jbasic.gwbasic.program.commands;

import ibmpc.devices.memory.MemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.OpCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * Randomize Command
 * <br>Syntax:  RANDOMIZE [var]
 * <br>Example: RANDOMIZE TIMER
 */
public class RandomizeOp extends GwBasicCommand {
  private Value value;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws jbasic.common.exceptions.JBasicException
   */
  public RandomizeOp( TokenIterator it ) throws JBasicException {
    parse( it );
  }

  /**
   * Executes this {@link OpCode opCode}
 * @throws jbasic.common.exceptions.JBasicException
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
    if( value != null ) {
      // get the value
      final MemoryObject object = value.getValue( program );

      // validate the value type
      if( !object.isNumeric() )
        throw new TypeMismatchException( object );

      // randomize
      program.randomize( object.toInteger() );
    }
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link TokenIterator iterator}
   * @throws JBasicException
   */
  private void parse( TokenIterator it ) throws JBasicException {
    if( it.hasNext() ) {
      // get the value
      value = GwBasicValues.getValueReference( it );

      // if any more tokens, error ...
      JBasicTokenUtil.noMoreTokens( it );
    }
  }

}
