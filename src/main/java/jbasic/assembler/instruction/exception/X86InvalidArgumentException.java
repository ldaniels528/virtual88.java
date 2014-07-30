/**
 * 
 */
package jbasic.assembler.instruction.exception;

import jbasic.assembler.instruction.element.X86DataElement;

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
