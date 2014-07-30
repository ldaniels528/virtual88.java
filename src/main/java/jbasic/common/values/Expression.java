package jbasic.common.values;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;

/**
 * Represents a logical expression
 * @author lawrence.daniels@gmail.com
 */
public interface Expression {
	
	/**
	 * Evaluates this expression
	 * @param program the currently running {@link JBasicCompiledCode program}
	 * @return true, if the result of the evaluation is true.
	 * @throws JBasicException
	 */
	boolean evaluate( JBasicCompiledCode program ) 
	throws JBasicException;
	
}
