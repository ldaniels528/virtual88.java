package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Invalid Opcode
 * 286+ computers execute INT 06H when executing and invalid opcode.
 * @author lawrence.daniels@gmail.com
 */
public class InvalidOpCodeInterrupt implements InterruptHandler {
	private static final InvalidOpCodeInterrupt instance = new InvalidOpCodeInterrupt();
	
	/**
	 * Private constructor
	 */
	private InvalidOpCodeInterrupt() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static InvalidOpCodeInterrupt getInstance() {
		return instance;
	}

	/**
	 * Process the interrupt 
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		
	}
	
}