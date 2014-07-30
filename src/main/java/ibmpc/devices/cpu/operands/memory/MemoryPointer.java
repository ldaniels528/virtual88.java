package ibmpc.devices.cpu.operands.memory;

/**
 * Represents a pointer to a memory address
 * @author lawrence.daniels@gmail.com
 */
public interface MemoryPointer extends OperandAddress {

	/**
	 * Returns the memory reference used by this pointer
	 * @return the {@link MemoryReference memory reference}
	 */
	MemoryReference getMemoryReference();
	
}
