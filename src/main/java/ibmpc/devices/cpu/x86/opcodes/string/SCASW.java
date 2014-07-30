package ibmpc.devices.cpu.x86.opcodes.string;

import static ibmpc.devices.cpu.operands.OperandUtil.compare;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * <pre>
 * Usage:  SCAS    string
 *         SCASB
 *         SCASW
 *         SCASD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Compares value at ES:DI (even if operand is specified) from the
 * accumulator and sets the flags similar to a subtraction.  DI is
 * incremented/decremented based on the instruction format (or
 * operand size) and the state of the Direction Flag.  Use with REP
 * prefixes.
 * </pre>
 */
public class SCASW extends AbstractOpCode {
	private static SCASW instance = new SCASW();
	
	/**
	 * Private constructor
	 */
	private SCASW() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static SCASW getInstance() {
		return instance;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the register collection and memory instances
		final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();
		
		// get the data word from DS:[SI]
		final int data0 = memory.getWord( cpu.DS.get(), cpu.SI.get() );
		
		// put the data word into ES:[DI]
		final int data1 = memory.getWord( cpu.ES.get(), cpu.DI.get() );
		
		// compare the data
		compare( cpu.FLAGS, data0, data1 );
		
		// setup increment/decrement value
		final int delta = cpu.FLAGS.isDF() ? -2 : 2;
		
		// increment/decrement pointers
		cpu.SI.add( delta );
		cpu.DI.add( delta );
	}

}