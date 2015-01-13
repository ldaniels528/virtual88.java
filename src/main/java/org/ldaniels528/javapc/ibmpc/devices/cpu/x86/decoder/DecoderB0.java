package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue16;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue8;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes B0 thru BF 
 * ------------------------------------------------------------------
 * instruction				code 		tt rrr mmm	iiii c rrr dddd
 * ------------------------------------------------------------------
 * mov al,nn				B0nn					1011 0 000 nn
 * mov cl,nn				B1nn					1011 0 001 nn
 * mov dl,nn				B2nn					1011 0 010 nn
 * mov bl,nn				B3nn					1011 0 011 nn
 * mov ah,nn				B4nn					1011 0 100 nn
 * mov ch,nn				B5nn					1011 0 101 nn
 * mov dh,nn				B6nn					1011 0 110 nn
 * mov bh,nn				B7nn					1011 0 111 nn
 * 
 * mov ax,nnnn				B8nnnn					1011 1 000 nnnn
 * mov cx,nnnn				B9nnnn					1011 1 001 nnnn
 * mov dx,nnnn				BAnnnn					1011 1 010 nnnn
 * mov bx,nnnn				BBnnnn					1011 1 011 nnnn
 * mov sp,nnnn				BCnnnn					1011 1 100 nnnn
 * mov bp,nnnn				BDnnnn					1011 1 101 nnnn
 * mov si,nnnn				BEnnnn					1011 1 110 nnnn
 * mov di,nnnn				BFnnnn					1011 1 111 nnnn
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class DecoderB0 implements Decoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode(final I8086 cpu, X86MemoryProxy proxy, DecodeProcessor processor) {
		// get the 8-bit instruction code
		final int code8 = proxy.nextByte();
		
		// decode the instruction
		switch( code8 ) {
			// 8-bit register MOV instructions
			case 0xB0:	return new MOV( cpu.AL, nextValue8( proxy ) );
			case 0xB1:	return new MOV( cpu.CL, nextValue8( proxy ) );
			case 0xB2:	return new MOV( cpu.DL, nextValue8( proxy ) );
			case 0xB3:	return new MOV( cpu.BL, nextValue8( proxy ) );
			case 0xB4:	return new MOV( cpu.AH, nextValue8( proxy ) );
			case 0xB5:	return new MOV( cpu.CH, nextValue8( proxy ) );
			case 0xB6:	return new MOV( cpu.DH, nextValue8( proxy ) );
			case 0xB7:	return new MOV( cpu.BH, nextValue8( proxy ) );
			
			// 16-bit register MOV instructions
			case 0xB8:	return new MOV( cpu.AX, nextValue16( proxy ) );
			case 0xB9:	return new MOV( cpu.CX, nextValue16( proxy ) );
			case 0xBA:	return new MOV( cpu.DX, nextValue16( proxy ) );
			case 0xBB:	return new MOV( cpu.BX, nextValue16( proxy ) );
			case 0xBC:	return new MOV( cpu.SP, nextValue16( proxy ) );
			case 0xBD:	return new MOV( cpu.BP, nextValue16( proxy ) );
			case 0xBE:	return new MOV( cpu.SI, nextValue16( proxy ) );
			case 0xBF:	return new MOV( cpu.DI, nextValue16( proxy ) );
			
			// must not happen
			default:	throw new UnhandledByteCodeException( code8 );
		}
	}

}
