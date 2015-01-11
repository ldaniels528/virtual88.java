package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * 
 * BIOS Parallel Port Services
 * @author lawrence.daniels@gmail.com
 */
public class ParallelPortServices implements InterruptHandler {
	private static final ParallelPortServices instance = new ParallelPortServices();
	
	/**
	 * Private constructor
	 */
	private ParallelPortServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static ParallelPortServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Parallel Port Services Interrupt (INT 17h)
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel8086 cpu)
	throws X86AssemblyException {
		// determine what to do
		switch( cpu.AH.get() ) {
			case 0x00: printCharacter(); break;
			case 0x01: initializePort(); break;
			case 0x02: readPortStatus(); break;
			default:
				throw new X86AssemblyException( "Invalid call (" + cpu.AH + ")" );
		}
	}
	
	/**
	 * Print character
	 *
	 */
	private void printCharacter() {
		// TODO build printer port object
	}

	/** 
	 * Initialize printer port
	 *
	 */
	private void initializePort() {
		// TODO build printer port object
	}
	
	/** 
	 * Read printer port status
	 * Returns:
	 * 	Status flags returned in register AH
	 *		?7?6?5?4?3?2?1?0? AH (status)
	 *		? ? ? ? ? ? ? +---- time-out
	 *		? ? ? ? ? +------- unused
	 *		? ? ? ? +-------- 1 = I/O error (~parallel~ pin 15)
	 *		? ? ? +--------- 1 = printer selected/on-line (parallel pin 13)
	 *		? ? +---------- 1 = out of paper (parallel pin 12)
	 *		? +----------- 1 = printer acknowledgment (parallel pin 10)
	 *		+------------ 1 = printer not busy (parallel pin 11)
	 */
	private void readPortStatus() {
		// TODO build printer port object
	}
	
}
