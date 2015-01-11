package org.ldaniels528.javapc.msdos.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Terminate Stay Resident (TSR) Services
 * @author ldaniels
 */
public class TerminateStayResidentServices implements InterruptHandler {
	private static final TerminateStayResidentServices instance = new TerminateStayResidentServices();
	
	/**
	 * Default constructor
	 */
	private TerminateStayResidentServices() {
		super();
	}

	/**
	 * @return the instance
	 */
	public static TerminateStayResidentServices getInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
	 */
	public void process(IbmPcSystem system, final Intel8086 cpu)
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		
	}

}
