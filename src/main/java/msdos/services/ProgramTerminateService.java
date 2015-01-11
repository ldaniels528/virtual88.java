/**
 * 
 */
package msdos.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * Program Terminate Service
 * @author ldaniels
 */
public class ProgramTerminateService implements InterruptHandler {
	private static final ProgramTerminateService instance = new ProgramTerminateService();
	
	/**
	 * Default constructor
	 */
	private ProgramTerminateService() {
		super();
	}

	/**
	 * @return the instance
	 */
	public static ProgramTerminateService getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(ibmpc.devices.cpu.Intel80x86)
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		cpu.halt();
	}

}
