/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler;

import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;

/**
 * 8086 Malformed Instruction Exception - Invalid Argument
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86InvalidArgumentException extends X86MalformedInstructionException {

	public X86InvalidArgumentException( final X86DataElement dataElement ) {
		super( "Invalid argument near '" + dataElement + "'" );
	}
	
}
