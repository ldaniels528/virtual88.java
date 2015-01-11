package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

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
	 * @see ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(ibmpc.devices.cpu.Intel80x86)
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		cpu.halt();
	}

}
