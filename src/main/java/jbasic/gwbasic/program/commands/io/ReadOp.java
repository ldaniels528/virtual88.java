package jbasic.gwbasic.program.commands.io;


import ibmpc.devices.memory.MemoryObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Variable;
import jbasic.common.values.VariableReference;
import jbasic.common.values.impl.SimpleConstant;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * READ Command 
 * <br>Syntax: READ <i>variable1<i>,[<i>variable2<i>,..]
 */
public class ReadOp extends GwBasicCommand {
  private final VariableReference[] references;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public ReadOp( TokenIterator it ) throws JBasicException {
    this.references = parse( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
      for( final VariableReference reference : references ) {
        // get the variable
        final Variable variable = reference.getVariable( program );
        
        // get the data constant
        final SimpleConstant constant = program.getNextData();
        
        // get the constant's value
        final MemoryObject value = constant.getValue( program );
        
        // set the value
        variable.setValue( value );
      }
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link Iterator iterator}
   * @throws jbasic.common.exceptions.JBasicException
   */
  private VariableReference[] parse( TokenIterator it ) throws JBasicException {
	  List<VariableReference> references = new LinkedList<VariableReference>();
      while( it.hasNext() ) {
        // add the variable reference
        references.add( GwBasicValues.getVariableReference( it ) );

        // there there are more elements; check for a command separator (comma)
        if( it.hasNext() ) JBasicTokenUtil.mandateToken( it, "," );
      }
      return references.toArray( new VariableReference[ references.size() ] );
  }

}
