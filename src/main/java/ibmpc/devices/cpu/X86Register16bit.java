package ibmpc.devices.cpu;

import ibmpc.devices.cpu.x86.registers.X86GenericRegister;

/**
 * Represents a 16-bit register implementation
 * @author lawrence.daniels@gmail.com
 */
public class X86Register16bit extends X86GenericRegister {

	/**
	 * Constructs a new 16-bit register
	 * @param name the name of this register
	 * @param index the given index of this register
	 */
	public X86Register16bit( String name, int index ) {
		super( name, 0xFFFF, index );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.X86Operand#size()
	 */
	public int size() {
		return SIZE_16BIT;
	}
	
}
