package ibmpc.devices.cpu.x86.opcodes.string.x386;

import static ibmpc.devices.cpu.operands.OperandUtil.compare;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.opcodes.string.REPNZ;
import ibmpc.devices.cpu.x86.opcodes.string.REPZ;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * <pre>
 * Usage:  CMPS    dest,src
 *         CMPSB
 *         CMPSW
 *         CMPSD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Subtracts destination value from source without saving results.
 * Updates flags based on the subtraction and  the index registers
 * (E)SI and (E)DI are incremented or decremented depending on the
 * state of the Direction Flag.  CMPSB inc/decrements the index
 * registers by 1, CMPSW inc/decrements by 2, while CMPSD increments
 * or decrements by 4.  The REP prefixes can be used to process
 * entire data items.
 *
 *                          Clocks                 Size
 * Operands         808x  286   386   486          Bytes
 *
 * dest,src          22    8     10    8             1  (W88=30)
 * </pre>
 * @see REPZ
 * @see REPNZ
 * @author lawrence.daniels@gmail.com
 */
public class CMPSD extends AbstractOpCode {
	private static CMPSD instance = new CMPSD();
	
	/**
	 * Private constructor
	 */
	private CMPSD() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static CMPSD getInstance() {
		return instance;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the RAM instance
		final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();
		
		// compare byte from DS:[SI] to ES:[DI]
		final int src = memory.getWord( cpu.DS.get(), cpu.SI.get() );
		final int dst = memory.getWord( cpu.ES.get(), cpu.DI.get() );
		
		// perform the comparison (update flags)
		compare( cpu.FLAGS, src, dst );
		
		// setup increment/decrement value
		final int delta = cpu.FLAGS.isDF() ? -4 : 4;
		
		// increment/decrement SI
		cpu.SI.add( delta );
	}

}