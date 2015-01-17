package org.ldaniels528.javapc.ibmpc.compiler.element.addressing;

import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;
import org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterRef;

/**
 * Represents an 8086 Memory Address
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86MemoryAddress implements X86DataElement {
	
	/**
	 * Default constructor
	 */
	public X86MemoryAddress() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSequence() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMemoryReference() {
		return true;
	}

	/**
	 * Indicates whether the memory address is a pointer
	 * @return true, if memory address is a pointer
	 */
	public boolean isMemoryPointer() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRegister() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return false;
	}
	
	/**
	 * Returns the byte code that represents the type
	 * of memory location that is being represented.
	 * @return the byte code
	 */
	public abstract byte[] getByteCode();
	
	/**
	 * Returns the byte code that represents the type
	 * of memory location that is being represented.
	 * @param reg the given {@link X86RegisterRef register}
	 * @return the byte code
	 */
	public abstract byte[] getByteCode( X86RegisterRef reg );

}
