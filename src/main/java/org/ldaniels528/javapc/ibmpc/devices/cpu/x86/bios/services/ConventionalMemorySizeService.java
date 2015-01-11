package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * BIOS Conventional Memory Size (INT 12h) Service
 * @author lawrence.daniels@gmail.com
 */
public class ConventionalMemorySizeService implements InterruptHandler {
	private static final ConventionalMemorySizeService instance = new ConventionalMemorySizeService();
	
	/**
	 * Private constructor
	 */
	private ConventionalMemorySizeService() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static ConventionalMemorySizeService getInstance() {
		return instance;
	}
	
	/**
	 * Process the Conventional Memory Size Interrupt
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		// get the RAM instance
		final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();
		
		// calculate free memory in Kilobytes
		final int memSize = memory.getWord( 0x0040, 0x0013 );
		
		// set the free memory into AX
		cpu.AX.set( memSize );
	}

}