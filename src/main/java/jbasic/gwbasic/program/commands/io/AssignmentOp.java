package jbasic.gwbasic.program.commands.io;

import ibmpc.devices.memory.MemoryObject;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.*;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

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
   * @see ibmpc.program.OpCode#execute(ibmpc.program.IbmPcProgram, ibmpc.IbmPcEnvironment)
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