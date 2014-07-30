package jbasic.common.program;

import jbasic.common.exceptions.JBasicException;

/**
 * A BASIC OpCode Instruction
 */
public interface OpCode {

	/**
	 * Executes this {@link jbasic.common.program.OpCode opCode}
	 * @param program the currently running {@link JBasicCompiledCode program}
	 * @throws JBasicException
	 */
	void execute( JBasicCompiledCode program ) throws JBasicException;

}
