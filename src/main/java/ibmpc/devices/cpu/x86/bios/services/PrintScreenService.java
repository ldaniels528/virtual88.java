package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * Print Screen / Bounds Exception
 *  INT 05H, to the dismay of Intel (which considers this a CPU-reserved
 *  interrupt), is used in the PC to execute the screen-to-printer dump
 *  routine in ROM BIOS.
 *
 *  It is called directly by the keyboard interrupt INT 09H when it senses the
 *  press of the PrtSc (or PrintScreen) key.  It may also be invoked by
 *  software, and you can intercept this interrupt if you want to write a
 *  custom screen-dump routine.
 *
 * The EGA BIOS has a function to replace the normal Print-Screen with one
 *  that works for a variety of screen character layouts.  See INT 10H 12H.
 *
 *  The DOS Graphics command replaces this routine with one that will print
 *  screen graphics (dot patterns) on a few types of printer.
 *
 * CPU Exception Interrupt?ó
 *  286+ computers execute INT 05H when the BOUND opcode is executed and the
 *  parameters are found to be out of range.
 *  </pre>
 * @author lawrence.daniels@gmail.com 
 */
public class PrintScreenService implements InterruptHandler {
	private static final PrintScreenService instance = new PrintScreenService();
	
	/**
	 * Private constructor
	 */
	private PrintScreenService() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static PrintScreenService getInstance() {
		return instance;
	}
	
	/**
	 * Process the interrupt
	 * @throws X86AssemblyException
	 */
	public void process( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		
	}

}
