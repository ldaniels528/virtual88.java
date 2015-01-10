package ibmpc.devices.cpu.x86.decoder;

import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressFar;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressNear;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressShort;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue8;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.x86.opcodes.flow.callret.CALL;
import ibmpc.devices.cpu.x86.opcodes.flow.jump.JCXZ;
import ibmpc.devices.cpu.x86.opcodes.flow.jump.JMP;
import ibmpc.devices.cpu.x86.opcodes.flow.loop.LOOP;
import ibmpc.devices.cpu.x86.opcodes.flow.loop.LOOPNZ;
import ibmpc.devices.cpu.x86.opcodes.flow.loop.LOOPZ;
import ibmpc.devices.cpu.x86.opcodes.io.IN;
import ibmpc.devices.cpu.x86.opcodes.io.OUT;
import ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes between E0h and EFh
 *	---------------------------------------------------------------------------
 *	type	bits		description 			comments
 *	---------------------------------------------------------------------------
 *	i		4	 		instruction type
 *	v		1			constant value			0=constant,1=non-constant
 *	x		1			??unknown??
 *	d		1			data direction			0=IN, 1=OUT
 *	c		1	 		memory class  			8-bit=0, 16-bit=1
 *
 *  Instruction code layout
 *  -----------------------------
 *  7654 3210 (8 bits)
 *  iiii vxdc
 *  
 * ---------------------------------------------------------------------------
 * instruction				code 	iiii v x d c 
 * ---------------------------------------------------------------------------
 * loopnz nn				E0 		1110 0 0 0 0 nn
 * loopz nn					E1 		1110 0 0 0 1 nn
 * loop nn					E2 		1110 0 0 1 0 nn
 * jcxz nn					E3 		1110 0 0 1 1 nn
 * in  al,nn				E4 		1110 0 1 0 0 nn
 * in  ax,nn				E5 		1110 0 1 0 1 nn
 * out nn,al				E6 		1110 0 1 1 0 nn
 * out nn,ax				E7 		1110 0 1 1 1 nn
 * call nnnn				E8 		1110 1 0 0 0 nnnn
 * jmp nnnn					E9 		1110 1 0 0 1 nnnn
 * jmp nnnn:nnnn			EA 		1110 1 0 1 0 nnnn nnnn
 * jmp nn					EB 		1110 1 0 1 1 nn
 * in  al,dx				EC		1110 1 1 0 0
 * in  ax,dx				ED		1110 1 1 0 1
 * out dx,al				EE		1110 1 1 1 0
 * out dx,ax				EF		1110 1 1 1 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class DecoderE0 implements Decoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode( final Intel80x86 cpu, final X86MemoryProxy proxy ) {
		// peek at the next byte
		final int code8 = proxy.nextByte();
		
		// decode the instruction
		switch( code8 ) {
			// LOOPNZ relOffset
			case 0xE0:	return new LOOPNZ( nextAddressShort( proxy ) );
			// LOOPZ relOffset
			case 0xE1:	return new LOOPZ( nextAddressShort( proxy ) );
			// LOOP relOffset
			case 0xE2:	return new LOOP( nextAddressShort( proxy ) );
			// JCXZ relOffset
			case 0xE3:	return new JCXZ( nextAddressShort( proxy ) );
			// IN AL,nn
			case 0xE4:	return new IN( cpu.AL, nextValue8( proxy ) );
			// IN AX,nn
			case 0xE5:	return new IN( cpu.AX, nextValue8( proxy ) );
			// OUT nn,AL
			case 0xE6:	return new OUT( nextValue8( proxy ), cpu.AL );
			// OUT nn,AX
			case 0xE7:	return new OUT( nextValue8( proxy ), cpu.AX );
			// CALL offset
			case 0xE8:	return new CALL( nextAddressNear( proxy ) );
			// JMP offset
			case 0xE9:	return new JMP( nextAddressNear( proxy ) );
			// JMP segment:offset
			case 0xEA:	return new JMP( nextAddressFar( cpu, proxy ) );
			// JMP relOffset
			case 0xEB:	return new JMP( nextAddressShort( proxy ) );
			// IN AL,DX
			case 0xEC:	return new IN( cpu.AL, cpu.DX );
			// IN AX,DX
			case 0xED:	return new IN( cpu.AX, cpu.DX );
			// OUT DX,AL
			case 0xEE:	return new OUT( cpu.DX, cpu.AL );
			// OUT DX,AX
			case 0xEF:	return new OUT( cpu.DX, cpu.AX );
			// unrecognized
			default:	throw new UnhandledByteCodeException( code8 );
		}
	}

}
