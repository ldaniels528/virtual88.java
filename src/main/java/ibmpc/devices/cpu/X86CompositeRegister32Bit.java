/**
 * 
 */
package ibmpc.devices.cpu;

import ibmpc.devices.cpu.x86.registers.X86CompositeRegister;
import ibmpc.devices.cpu.x86.registers.X86Register;

/**
 * Represents a 32-bit composite register; a register composed of
 * two parts, high and low respectively. (e.g. EAX = <unknown>:EAX)
 * @author lawrence.daniels@gmail.com
 */
public class X86CompositeRegister32Bit extends X86CompositeRegister {

	/**
	 * Default Constructor
	 * @param name the name of this register
	 * @param hi the high portion of this register
	 * @param lo the low portion of this register
	 * @param index the given register index
	 */
	public X86CompositeRegister32Bit( final String name, 
									  final X86Register hi, 
									  final X86Register lo, 
									  final int index) {
		super( name, hi, lo, 8, index );
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.X86Operand#size()
	 */
	public int size() {
		return SIZE_32BIT;
	}

}
