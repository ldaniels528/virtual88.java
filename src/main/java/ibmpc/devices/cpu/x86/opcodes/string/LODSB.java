package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * <pre>
 * Usage: LODS src
 *        LODSB
 *        LODSW
 *        LODSD (386+)
 *
 * Modifies Flags: None
 *
 * Transfers string element addressed by DS:SI (even if 
 * an operand is supplied) to the accumulator. SI is incremented 
 * based on the size of the operand or based on the instruction 
 * used. If the Direction Flag is set SI is decremented, if 
 * the Direction Flag is clear SI is incremented. Use with REP
 * prefixes.
 * </pre>
 * @see REPNZ
 * @see REPZ
 * @author lawrence.daniels@gmail.com
 */
public class LODSB extends AbstractOpCode {
	private static LODSB instance = new LODSB();
	
	/**
	 * Private constructor
	 */
	private LODSB() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static LODSB getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute( final Intel80x86 cpu ) {
		// get the RAM instance
		final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();
		
		// load byte at DS:[SI] into AL
		cpu.AL.set( memory.getByte( cpu.DS.get(), cpu.SI.get() ) );
		
		// setup increment/decrement value
		final int delta = cpu.FLAGS.isDF() ? -1 : 1;
		
		// increment/decrement SI
		cpu.SI.add( delta );
 	}

}