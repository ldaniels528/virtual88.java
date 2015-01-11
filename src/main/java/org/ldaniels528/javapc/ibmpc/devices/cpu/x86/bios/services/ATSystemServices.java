package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * BIOS AT System Services
 * @author lawrence.daniels@gmail.com
 */
public class ATSystemServices implements InterruptHandler {
	private static final ATSystemServices instance = new ATSystemServices();
	
	/**
	 * Private constructor
	 */
	private ATSystemServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static ATSystemServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Disk Services Interrupt (INT 15h)
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
		// determine what to do
		switch( cpu.AH.get() ) {
			default:
				throw new X86AssemblyException( "Invalid call (" + cpu.AH + ")" );
		}
	}
}
