package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * Assignment Operation
 * <br>Syntax: [LET] <variable> = <value|variable>
 * <br>Example: X = 7
 */
public class AssignmentOp extends GwBasicCommand {
	private final VariableReference reference;
	private final Value value;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public AssignmentOp( TokenIterator it ) throws JBasicException {
	    // get the variable reference
	    reference = GwBasicValues.getVariableReference( it );

	    // next token must be equal operator (=)
	    JBasicTokenUtil.mandateToken( it, "=" );

	    // get the value
	    value = GwBasicValues.getValueReference( it );
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.ibmpc.program.OpCode#execute(org.ldaniels528.javapc.ibmpc.program.IbmPcProgram, org.ldaniels528.javapc.ibmpc.IbmPcEnvironment)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // get the variable
	  final Variable variable = reference.getVariable( program );

	  // get the value
	  final MemoryObject object = value.getValue( program );
    
	  // assign the value to the variable
	  variable.setValue( object );
  }

}