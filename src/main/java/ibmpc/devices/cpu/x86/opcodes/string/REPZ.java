package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * Usage:  REPE
 *         REPZ
 *  Modifies flags: None
 *
 *  Repeats execution of string instructions while CX != 0 and the Zero
 *  Flag is set.  CX is decremented and the Zero Flag tested after
 *  each string operation.   The combination of a repeat prefix and a
 *  segment override on processors other than the 386 may result in
 *  errors if an interrupt occurs before CX=0.
 * </pre>
 * @see CMPSB
 * @see CMPSW
 * @see LODSB
 * @see LODSW
 * @see SCASB
 * @see SCASW
 * @see STOSB
 * @see STOSW
 * @author lawrence.daniels@gmail.com
 */
public class REPZ extends AbstractOpCode {
	private final OpCode opCode;
	
	/**
	 * Creates a new REPZ opCode
	 * @param opCode the given {@link OpCode opCode}
	 */
	public REPZ( final OpCode opCode ) {
		this.opCode = opCode;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// cache CX and FLAGS
		final X86Register CX = cpu.CX;
		final X86ExtendedFlags FLAGS = cpu.FLAGS;
		
		// is it a mass data instruction?
		if( opCode instanceof MassDataOpCode ) {
			if( ( CX.get() != 0 ) && !FLAGS.isZF() ) {
				final MassDataOpCode massDataOpCode = (MassDataOpCode)opCode;
				massDataOpCode.executeEnMass( cpu, CX.get() );
				CX.set( 0 );
			}
		}
		
		// execute normally
		else {
			// while the CX != 0 and ZF=0
			while( ( CX.get() != 0 ) && !FLAGS.isZF() ) {
				// execute the instruction
				cpu.execute( opCode );
				CX.add( -1 );
			}
		}
		
		// set the Zero Flag (ZF)
		FLAGS.setZF( true );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "REPZ %s", opCode );
	}
	
}