package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;

/**
 * BIOS Keyboard Hardware Services Processor
 * @author lawrence.daniels@gmail.com
 */
public class KeyboardHardwareServices implements InterruptHandler {
	private static final KeyboardHardwareServices instance = new KeyboardHardwareServices();
	
	/**
	 * Private constructor
	 */
	private KeyboardHardwareServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static KeyboardHardwareServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Disk Services Interrupt (INT 13h)
	 * @throws X86AssemblyException
	 */
	public void process( Intel80x86 cpu ) throws X86AssemblyException {
		// determine what to do
		switch( cpu.AH.get() ) {
			default:
				throw new X86AssemblyException( "Invalid call (" + cpu.AH + ")" );
		}
	}
}
