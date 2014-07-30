package jbasic.assembler.instruction.element.addressing;

import jbasic.assembler.instruction.element.X86DataElement;
import jbasic.assembler.instruction.element.registers.X86RegisterRef;

/**
 * Represents an 80x86 Memory Address
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86MemoryAddress implements X86DataElement {
	
	/**
	 * Default constructor
	 */
	public X86MemoryAddress() {
		super();
	}

	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#getSequence()
	 */
	public int getSequence() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#isMemoryReference()
	 */
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

	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#isRegister()
	 */
	public boolean isRegister() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#isValue()
	 */
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
