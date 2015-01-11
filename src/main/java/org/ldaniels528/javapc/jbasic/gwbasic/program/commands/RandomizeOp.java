package org.ldaniels528.javapc.jbasic.gwbasic.program.commands;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.OpCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

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
   * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
   */
  public RandomizeOp( TokenIterator it ) throws JBasicException {
    parse( it );
  }

  /**
   * Executes this {@link OpCode opCode}
 * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
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
   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
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
