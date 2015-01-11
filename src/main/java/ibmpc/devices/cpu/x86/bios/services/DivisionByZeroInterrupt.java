package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * The CPU automatically executes this interrupt after a DIV or IDIV is
 *  performed and the result (quotient) of a division operation will not fit
 *  into the destination.  This includes division by 0.  For instance...
 * 
 *            mov   ax,4000H  ;dividend
 *            mov   bl,2      ;divisor is byte so destination is byte (AL)
 *            div   bl        ;4000H/2 is 2000H which will not fit in AL
 *                           ;  so INT 00H is executed
 * ...or simply:...
 *           mov   bx,0
 *           div   bx        ;any division by 0
 * </pre>
 */
public class DivisionByZeroInterrupt implements InterruptHandler {
	private static final DivisionByZeroInterrupt instance = new DivisionByZeroInterrupt();
	
	/**
	 * Private constructor
	 */
	private DivisionByZeroInterrupt() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class
	 * @return the singleton instance of this class 
	 */
	public static DivisionByZeroInterrupt getInstance() {
		return instance;
	}
	
	/**
	 * Process the Division By Zero Interrupt (INT 00h)
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		// TODO do something here
	}

}
