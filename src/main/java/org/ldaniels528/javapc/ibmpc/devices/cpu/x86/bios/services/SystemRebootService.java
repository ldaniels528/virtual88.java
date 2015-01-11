package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * System Reboot Service
 * @author ldaniels
 */
public class SystemRebootService implements InterruptHandler {
	private static final SystemRebootService instance = new SystemRebootService();
	
	/**
	 * Private constructor
	 */
	private SystemRebootService() {
		super();
	}
	
	/**
	 * @return the instance
	 */
	public static SystemRebootService getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
	 */
	public void process(IbmPcSystem system, final Intel8086 cpu)
	throws X86AssemblyException {
		cpu.halt();
	}

}
