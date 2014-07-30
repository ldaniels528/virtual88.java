package msdos.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import ibmpc.exceptions.X86AssemblyException;

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
	 * @see ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(ibmpc.devices.cpu.Intel80x86)
	 */
	public void process( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		
	}

}
