package ibmpc.devices.cpu.x86.opcodes.io;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * INS/INSB/INSW/INSD -- Input from Port to String
 * 
 * Opcode  Instruction    Clocks         Description
 * 6C      INS r/m8,DX    15,pm=9/29**  Input byte from port DX into ES:(E)DI
 * 6D      INS r/m16,DX   15,pm=9/29**  Input word from port DX into ES:(E)DI
 * 6D      INS r/m32,DX   15,pm=9/29**  Input dword from port DX into ES:(E)DI
 * 6C      INSB           15,pm=9/29**  Input byte from port DX into ES:(E)DI
 * 6D      INSW           15,pm=9/29**  Input word from port DX into ES:(E)DI
 * 6D      INSD           15,pm=9/29**  Input dword from port DX into ES:(E)DI
 * </pre>
 * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/INS.htm">INS</a>
 * @author lawrence.daniels@gmail.com
 */
public class INSB extends AbstractOpCode {
	private static INSB instance = new INSB();
	
	/**
	 * Private constructor
	 */
	private INSB() {
		super();
	}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static INSB getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// TODO finish this instruction
	}

}
