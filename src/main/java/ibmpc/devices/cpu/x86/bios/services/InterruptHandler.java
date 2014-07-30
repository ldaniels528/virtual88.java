package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Represents a generic interrupt handler
 * @author ldaniels
 */
public interface InterruptHandler {
	
	/**
	 * Process the Equipment Services Interrupt (INT 11h)
	 * @param cpu the given {@link IbmPcSystem IBM PC System} instance
	 * @throws X86AssemblyException
	 */
	void process( Intel80x86 cpu ) throws X86AssemblyException;

}
