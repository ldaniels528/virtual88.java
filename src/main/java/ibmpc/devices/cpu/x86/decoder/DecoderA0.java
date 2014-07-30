package ibmpc.devices.cpu.x86.decoder;

import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.*;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.x86.opcodes.data.MOV;
import ibmpc.devices.cpu.x86.opcodes.math.TEST;
import ibmpc.devices.cpu.x86.opcodes.string.CMPSB;
import ibmpc.devices.cpu.x86.opcodes.string.CMPSW;
import ibmpc.devices.cpu.x86.opcodes.string.LODSB;
import ibmpc.devices.cpu.x86.opcodes.string.LODSW;
import ibmpc.devices.cpu.x86.opcodes.string.MOVSB;
import ibmpc.devices.cpu.x86.opcodes.string.MOVSW;
import ibmpc.devices.cpu.x86.opcodes.string.SCASB;
import ibmpc.devices.cpu.x86.opcodes.string.SCASW;
import ibmpc.devices.cpu.x86.opcodes.string.STOSB;
import ibmpc.devices.cpu.x86.opcodes.string.STOSW;
import ibmpc.devices.cpu.x86.opcodes.string.x386.CMPSD;
import ibmpc.devices.cpu.x86.opcodes.string.x386.LODSD;
import ibmpc.devices.cpu.x86.opcodes.string.x386.MOVSD;
import ibmpc.devices.cpu.x86.opcodes.string.x386.SCASD;
import ibmpc.devices.cpu.x86.opcodes.string.x386.STOSD;
import ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes A0 thru AF.
 *	---------------------------------------------------------------------------
 *	type	bits	description 				comments
 * 	---------------------------------------------------------------------------
 *	i		4	 	instruction type  	
 *	j		3		instruction sub type		
 *	c		1	 	memory class  				8-bit=0, 16-bit=1
 * 	d		8/16	data value					(optional)
 * 
 * ---------------------------------------------------------------------------
 * instruction				code 		iiii jjj c dddd
 * --------------------------------------------------------------------------- 
 * mov al,[nnnn]			A0nnnn		1010 000 0 nnnn
 * mov ax,[nnnn]			A1nnnn		1010 000 1 nnnn
 * mov [nnnn],al			A2nnnn		1010 001 0 nnnn
 * mov [nnnn],ax			A3nnnn		1010 001 1 nnnn
 * movsb					A4			1010 010 0
 * movsw					A5			1010 010 1
 * cmpsb					A6			1010 011 0
 * cmpsw					A7			1010 011 1
 * test al,nn				A8nn		1010 100 0 nn 
 * test ax,nnnn				A9nnnn		1010 100 1 nnnn 
 * stosb					AA			1010 101 0
 * stosw					AB			1010 101 1
 * lodsb					AC			1010 110 0
 * lodsw					AD			1010 110 1
 * scasb					AE			1010 111 0
 * scasw					AF			1010 111 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class DecoderA0 implements Decoder {

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.decoders.I8086Decoder#decode(ibmpc.devices.cpu.VirtualCPU)
	 */
	public OpCode decode( final Intel80x86 cpu, final X86MemoryProxy proxy ) {
		// peek at the next word
		final int code8  = proxy.nextByte();
		
		// is virtual mode set?
		final boolean virtualMode = cpu.isVirtualMode();
		
		// evaluate the code
		switch( code8 ) {
			// MOV AL,[nnnn]
			case 0xA0: 	return new MOV( cpu.AL, nextAddressByte( cpu, proxy ) ); 
			// MOV AX,[nnnn]
			case 0xA1:	return new MOV( cpu.AX, nextAddressWord( cpu, proxy ) ); 
			// MOV [nnnn],AL
			case 0xA2:	return new MOV( nextAddressByte( cpu, proxy ), cpu.AL ); 
			// MOV [nnnn],AX
			case 0xA3: 	return new MOV( nextAddressWord( cpu, proxy ), cpu.AX ); 
			// MOVSB
			case 0xA4: 	return MOVSB.getInstance(); 
			// MOVSW
			case 0xA5: 	return virtualMode ? MOVSD.getInstance() : MOVSW.getInstance(); 
			// CMPSB
			case 0xA6: 	return CMPSB.getInstance(); 
			// CMPSW
			case 0xA7: 	return virtualMode ? CMPSD.getInstance() : CMPSW.getInstance(); 
			// TEST AL,nn
			case 0xA8:	return new TEST( cpu.AL, nextAddressByte( cpu, proxy ) );
			// TEST AX,nnnn
			case 0xA9:	return new TEST( cpu.AX, nextAddressWord( cpu, proxy ) );
			// STOSB
			case 0xAA: 	return STOSB.getInstance(); 
			// STOSW
			case 0xAB: 	return virtualMode ? STOSD.getInstance() : STOSW.getInstance(); 
			// LODSB
			case 0xAC: 	return LODSB.getInstance(); 
			// LODSW
			case 0xAD: 	return virtualMode ? LODSD.getInstance() : LODSW.getInstance(); 
			// SCASB
			case 0xAE: 	return SCASB.getInstance(); 
			// SCASW
			case 0xAF: 	return virtualMode ? SCASD.getInstance() : SCASW.getInstance(); 
			// unrecognized
			default:	throw new UnhandledByteCodeException( code8 );
		}
	}
	
}
