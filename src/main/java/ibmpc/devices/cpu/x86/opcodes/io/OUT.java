package ibmpc.devices.cpu.x86.opcodes.io;

import static ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static ibmpc.devices.cpu.operands.Operand.SIZE_32BIT;
import static ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.devices.ports.IbmPcHardwarePorts;

/** 
 * <pre>
 * Output Data to Port
 * 
 * Usage:  OUT     port,accum
 * Modifies flags: None
 *
 * Transfers byte in AL, word in AX, or dword in EAX to the specified
 * hardware port address.  If the port number is in the range of 0-255
 * it can be specified as an immediate.  If greater than 255 then the
 * port number must be specified in DX.  Since the PC only decodes 10
 * bits of the port address, values over 1023 can only be decoded by
 * third party vendor equipment and also map to the port range 0-1023.
 * </pre>
 * @see IN
 * @author lawrence.daniels@gmail.com
 */
public class OUT extends AbstractOpCode {
	private final Operand accum;
	private final Operand port;
	
	/**
	 * OUT port, accum
	 * @param port the given {@link Operand port}
	 * @param accum the given {@link X86Register accumulator}
	 */
	public OUT( final Operand port, final X86Register accum ) {
		this.port	= port;
		this.accum	= accum;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
	 */
	public void execute( final Intel80x86 cpu ) {
		// get the port number
		final int portNum = port.get();
		
		// get the data value
		final int value	  = accum.get();
		
		// output the value to the port
		final IbmPcHardwarePorts ports = cpu.getHardwarePorts();
		switch( accum.size() ) {
			case SIZE_8BIT:	 ports.out8( portNum, value ); break;
			case SIZE_16BIT: ports.out16( portNum, value ); break;
			case SIZE_32BIT: ports.out32( portNum, value ); break;
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
	 */
	public String toString() {
		return String.format( "OUT %s,%s", port, accum );
	}
	
}
