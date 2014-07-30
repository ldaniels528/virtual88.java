package ibmpc.devices.cpu.x86.opcodes.data.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * CWD/CDQ -- Convert Word to Doubleword/Convert Doubleword to Quadword
 * 
 * Opcode    Instruction        Clocks   Description
 * 99        CWD                2        DX:AX := sign-extend of AX
 * 99        CDQ                2        EDX:EAX := sign-extend of EAX
 * 
 * Operation
 * IF OperandSize = 16 (* CWD instruction *)
 * THEN
 *  IF AX < 0 THEN DX := 0FFFFH; ELSE DX := 0; FI;
 *  ELSE (* OperandSize = 32, CDQ instruction *)
 *  IF EAX < 0 THEN EDX := 0FFFFFFFFH; ELSE EDX := 0; FI;
 * FI;
 * 
 * Description
 * CWD converts the signed word in AX to a signed doubleword in DX:AX 
 * by extending the most significant bit of AX into all the bits of DX. 
 * CDQ converts the signed doubleword in EAX to a signed 64-bit integer 
 * in the register pair EDX:EAX by extending the most significant bit 
 * of EAX (the sign bit) into all the bits of EDX. Note that CWD is 
 * different from CWDE. CWDE uses EAX as a destination, instead of DX:AX.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class CDQ extends AbstractOpCode {
	private static CDQ instance = new CDQ();
	
	/**
	 * Private constructor
	 */
	private CDQ() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static CDQ getInstance() {
		return instance;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the signed 32-bit integer from EAX
		final int signedInt32 = cpu.EAX.get();
		
		// get the sign of the 32-bit integer
		// mask 1000 0000 0000 0000 0000 0000 0000 0000
		final int high32 = ( signedInt32 & 0x80000000 );
		
		// get the unsigned 32-bit integer
		// mask 0111 1111 1111 1111 1111 1111 1111 1111
		final int low32	 = ( signedInt32 & 0x7FFFFFFF );
				
		// put the high portion in EDX and the low portion in EAX
		cpu.EDX.set( high32 );
		cpu.EAX.set( low32 );
	}

}
