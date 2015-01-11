package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupOperands;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue16;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue8;
import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.ADC;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.SBB;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.POP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.PUSH;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes between 10h and 1Fh
 *	---------------------------------------------------------------------------
 *	type	bits	description 				comments
 * 	---------------------------------------------------------------------------
 *	t		2		register/reference type		see X86AddressReferenceTypes
 *	r		3		register info				see X86RegisterReferences
 *	m		3		memory reference info		see X86AddressReferenceTypes
 *	i		6	 	instruction type  			
 *	s		1		source/signed				register=0,reference=1 / 1='+',0='-'
 *	c		1	 	memory class  				8-bit=0, 16-bit=1
 * 	d		16/32	offset						(optional)
 *
 *  Instruction code layout
 *  ------------------------------
 *  fedc ba98 7654 3210 
 *  		  iiii iisc ( 8 bits)
 *  ttrr rmmm iiii iisc (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		tt rrr mmm	iiiii x s c dddd
 * ------------------------------------------------------------------
 * adc	[bx],al				0710		00 000 111 	00010 0 0 0
 * adc	[bx],ax				0711		00 000 111 	00010 0 0 1
 * adc	al,[bx]				0712		00 000 111 	00010 0 1 0
 * adc	ax,[bx]				0713		00 000 111 	00010 0 1 1
 * adc	al,nn				  14					00010 1 0 0 nn
 * adc	ax,nnnn				  15					00010 1 0 1 nnnnn
 * push ss					  16					00010 1 1 0
 * pop ss					  17					00010 1 1 1
 * sbb	[bx],al				0718		00 000 111 	00011 0 0 0
 * sbb	[bx],ax				0719		00 000 111 	00011 0 0 1 
 * sbb	al,[bx]				071A		00 000 111 	00011 0 1 0 
 * sbb	ax,[bx]				071B		00 000 111 	00011 0 1 1 
 * sbb	al,nn			  	  1C					00011 1 0 0 nn
 * sbb	ax,nnnn				  1D					00011 1 0 1 nnnnn
 * push ds					  1E					00011 1 1 0
 * pop ds					  1F					00011 1 1 1
 * </pre> 
 * @author lawrence.daniels@gmail.com
 */
public class Decoder10 implements Decoder {
	// define the complex instruction code constants
	private static final int ADC_CODE 	= 0x02;	// 00010
	private static final int SBB_CODE 	= 0x03;	// 00011

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode( final Intel8086 cpu, final X86MemoryProxy proxy ) {
		// get the 8-bit instruction
		final int code8 = proxy.nextByte();
		
		// decode the instruction
		switch( code8 ) {
			// ADC AL,nn
			case 0x14:	return new ADC( cpu.AL, nextValue8( proxy ) );
			// ADC AX,nnnn
			case 0x15:	return new ADC( cpu.AX, nextValue16( proxy ) );
			// PUSH SS
			case 0x16:	return new PUSH( cpu.SS );
			// POP SS
			case 0x17:	return new POP( cpu.SS );
			// SBB AL,nn
			case 0x1C:	return new SBB( cpu.AL, nextValue8( proxy ) );
			// SBB AX,nnnn
			case 0x1D:	return new SBB( cpu.AX, nextValue16( proxy ) );
			// PUSH DS
			case 0x1E:	return new PUSH( cpu.DS );
			// POP DS
			case 0x1F:	return new POP( cpu.DS );
			// for all others ...
			default:	return decodeComplexCode( cpu, proxy, code8 );
		}
	}
	
	/**
	 * Decodes complex instruction codes between 00h and 0Fh
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy}
	 * @param code8 the given 8-bit instruction code
	 * @return the resultant {@link OpCode opCode}, 
	 * or <tt>null</tt> if not found
	 */
	private OpCode decodeComplexCode( final Intel8086 cpu, final X86MemoryProxy proxy, final int code8 ) {
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// mmrr rmmm iiii ivsc 
		
		// get the 16-bit instruction code
		final int code16 = proxy.nextWord( code8 );
		
		// get data elements
		final Operand[] operands = lookupOperands( cpu, proxy, code16, true, false );
		
		// identify the destination and source operands
		final Operand dest = operands[0];
		final Operand src = operands[1];
		
		// get the instruction code
		// code: .... .... iiii i... (mask = 0000 0000 1111 1000) 
		final int insCode = ( code16 & 0x00F8 ) >> 3;	
		
		// evaluate the instruction
		switch( insCode ) {
			case ADC_CODE:	return new ADC( dest, src ); 
			case SBB_CODE:	return new SBB( dest, src ); 
			default:		throw new UnhandledByteCodeException( code16 );
		}
	}

}
