package ibmpc.devices.cpu.x86.opcodes.string.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.opcodes.string.REPNZ;
import ibmpc.devices.cpu.x86.opcodes.string.REPZ;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;

/** 
 * <pre>
 * Copies data from addressed by DS:SI (even if operands are given) to
 * the location ES:DI destination and updates SI and DI based on the
 * size of the operand or instruction used.  SI and DI are incremented
 * when the Direction Flag is cleared and decremented when the Direction
 * Flag is Set.  Use with REP prefixes. 
 * </pre>
 * @see REPZ
 * @see REPNZ
 * @author lawrence.daniels@gmail.com
 */
public class MOVSD extends AbstractOpCode {
	private static MOVSD instance = new MOVSD();
	
	/**
	 * Private constructor
	 */
	private MOVSD() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static MOVSD getInstance() {
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
		final int data = memory.getWord( cpu.DS.get(), cpu.SI.get() );
		
		// put the data word into ES:[DI]
		memory.setWord( cpu.ES.get(), cpu.DI.get(), data );
		
		// setup increment/decrement value
		final int delta = cpu.FLAGS.isDF() ? -4 : 4;
		
		// increment/decrement SI and DI
		cpu.SI.add( delta );
		cpu.DI.add( delta );
	}

}
