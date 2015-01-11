/**
 * 
 */
package org.ldaniels528.javapc.msdos.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

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
	 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		cpu.halt();
	}

}
