package ibmpc.devices.cpu.x86.opcodes.stack;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86Stack;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.opcodes.stack.x386.PUSHFD;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 *  Usage:  PUSHF
 *          PUSHFD  (386+)
 *  Modifies flags: None
 *
 *  Transfers the Flags Register onto the stack.  PUSHF saves a 16 bit
 *  value while PUSHFD saves a 32 bit value.
 *
 *                           Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *
 *  none            10/14   3     4     4             1
 *  none  (PM)        -     -     4     3             1
 * </pre>
 * @see PUSHFD
 * @author lawrence.daniels@gmail.com
 */
public class PUSHF extends AbstractOpCode {
	private static PUSHF instance = new PUSHF();
	
	/**
	 * Private constructor
	 */
	private PUSHF() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static PUSHF getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		final X86Stack stack = cpu.getStack();
		stack.push( cpu.FLAGS );
	}

}
