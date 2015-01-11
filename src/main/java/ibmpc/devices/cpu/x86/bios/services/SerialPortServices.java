package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * BIOS Serial Communication Services Processor
 * @author lawrence.daniels@gmail.com
 */
public class SerialPortServices implements InterruptHandler {
	private static final SerialPortServices instance = new SerialPortServices();
	
	/**
	 * Private constructor
	 */
	private SerialPortServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static SerialPortServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Disk Services Interrupt (INT 14h)
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		// determine what to do
		switch( cpu.AH.get() ) {
			default:
				throw new X86AssemblyException( "Invalid call (" + cpu.AH + ")" );
		}
	}
}
