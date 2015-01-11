/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler.exception;

import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;

/**
 * 80x86 Malformed Instruction Exception - Invalid Argument
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86InvalidArgumentException extends X86MalformedInstructionException {

	public X86InvalidArgumentException( final X86DataElement dataElement ) {
		super( "Invalid argument near '" + dataElement + "'" );
	}
	
}
