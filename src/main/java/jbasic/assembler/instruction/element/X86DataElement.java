/**
 * 
 */
package jbasic.assembler.instruction.element;

import jbasic.assembler.instruction.element.addressing.X86MemoryAddress;
import jbasic.assembler.instruction.element.registers.X86RegisterRef;

/**
 * Represents an 80x86 Data Element (e.g. {@link X86RegisterRef register} 
 * or {@link X86MemoryAddress memory reference})
 * @author lawrence.daniels@gmail.com
 */
public interface X86DataElement {
	
	/**
	 * Returns the sequence number of the data element
	 * @return the sequence number of the data element
	 */
	int getSequence();

	/**
	 * Indicates whether the data element is a register
	 * @return true, if the data element is a register
	 */
	boolean isRegister();
	
	/**
	 * Indicates whether the data element is a memory reference
	 * @return true, if the data element is a memory reference
	 */
	boolean isMemoryReference();
	
	/**
	 * Indicates whether the data element is a numeric value
	 * @return true, if the data element is a numeric value
	 */
	boolean isValue();
	
}
