/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler;

import org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterRef;

/**
 * 80x86 Malformed Instruction Exception - Invalid register usage
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86InvalidRegisterUsageException extends X86MalformedInstructionException {
	private final X86RegisterRef register;
	
	/**
	 * Creates a new invalid register usage exception
	 * @param register the given {@link X86RegisterRef register}
	 */
	public X86InvalidRegisterUsageException( final X86RegisterRef register ) {
		super( "Invalid register usage near '" + register + "'" );
		this.register = register;
	}

	/**
	 * @return the {@link X86RegisterRef 80x86 register}
	 */
	public X86RegisterRef getRegister() {
		return register;
	}
	
}
