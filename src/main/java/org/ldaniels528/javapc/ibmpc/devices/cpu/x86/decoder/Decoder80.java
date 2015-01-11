package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupOperands;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupReferencedAddress;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupRegister;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupSecondaryOperand;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextSignedValue8;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue;
import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing.LEA;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise.AND;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise.OR;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise.XOR;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.XCHG;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.ADC;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.ADD;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.CMP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.SBB;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.SUB;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.TEST;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.POP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Processes instruction codes from 80h to 8Fh
 * ---------------------------------------------------------------------------
 * type		bits		description 				comments
 * ---------------------------------------------------------------------------
 * t		2			register/reference type		11=register	
 * r		3			register info				ax=000	
 * m		3			memory reference info		[bx]=000
 * i		5	 		instruction type  			
 * s		1			source (primary)			register=0, reference=1
 * c		1	 		memory class 	 			8-bit=0, 16-bit=1	
 * d		8/16		direct value 
 *  
 * ------------------------------------------------------------------
 * Type A
 * instruction				code 		tt jjj rrr	iiiiii k c dddd
 * ------------------------------------------------------------------
 * add cx,nnnn				C181nnnn	11 000 001	100000 0 1 nnnn
 * or  cx,nnnn				C981nnnn	11 001 001	100000 0 1 nnnn
 * adc cx,nnnn				D181nnnn	11 010 001	100000 0 1 nnnn
 * sbb cx,nnnn				D981nnnn	11 011 001	100000 0 1 nnnn
 * and cx,nnnn				E181nnnn	11 100 001	100000 0 1 nnnn
 * sub cx,nnnn				E981nnnn	11 101 001	100000 0 1 nnnn 
 * xor cx,nnnn				F181nnnn	11 110 001	100000 0 1 nnnn
 * cmp cx,nnnn				F981nnnn	11 111 001	100000 0 1 nnnn
 * 
 * add bx,+nn				C383nn		11 000 011	100000 1 1 nn
 * or  bx,+nn				CB83nn		11 001 011	100000 1 1 nn
 * adc bx,+nn				D383nn		11 010 011 	100000 1 1 nn
 * sbb bx,+nn				DB83nn		11 011 011 	100000 1 1 nn
 * and bx,+nn				E383nn		11 100 011 	100000 1 1 nn
 * sub bx,+nn				EB83nn		11 101 011 	100000 1 1 nn
 * xor bx,+nn				F383nn		11 110 011 	100000 1 1 nn
 * cmp bx,+nn				FB83nn		11 111 011 	100000 1 1 nn
 *  
 * ------------------------------------------------------------------
 * Type B
 * instruction				code 		tt rrr mmm	iiiiii j c dddd
 * ------------------------------------------------------------------
 * test	al,[bx]				0784		00 000 111 	100001 0 0
 * test al,cl				C184		11 000 001 	100001 0 0 
 * test	ax,[bx]				0785		00 000 111 	100001 0 1
 * test ax,cx				C185		11 000 001 	100001 0 1 
 * 
 * xchg	al,[bx]				0786		00 000 111 	100001 1 0
 * xchg al,cl				C186		11 000 001 	100001 1 0 
 * xchg	ax,[bx]				0787		00 000 111 	100001 1 1
 * xchg ax,cx				C187		11 000 001 	100001 1 1
 * 
 * ------------------------------------------------------------------
 * Type C
 * instruction				code 		tt rrr mmm	iiiiii s c dddd
 * ------------------------------------------------------------------
 * mov al,al				C088		11 000 000	100010 0 0 
 * mov cl,al				C188		11 000 001	100010 0 0 
 * mov al,cl				C888		11 001 000	100010 0 0 
 * mov [bx],ax				0789		00 000 111	100010 0 1 
 * mov ax,ax				C089		11 000 000	100010 0 1 
 * mov ax,cx				C889		11 001 000	100010 0 1 
 * mov al,[nnnn]			0E8A		00 001 110	100010 1 0
 * mov ax,[bx]				078B		00 000 111	100010 1 1 
 * 
 * ------------------------------------------------------------------
 * Type D
 * instruction				code 		tt rrr mmm	iiiiii jj dddd
 * ------------------------------------------------------------------
 * mov ax,cs				C88C		11 001 000	100011 00 
 * lea ax,[bx]				078D		00 000 111 	100011 01
 * mov cs,ax				C88E		11 001 000	100011 10 
 * pop [bx]					078F		00 000 111	100011 11  
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Decoder80 implements Decoder {
	// define Type A instruction codes
	private static final int INS_TYPE_A 		= 0x20; // 100000
	private static final int INS_SUB_TYPE_ADD	= 0x00; // 000
	private static final int INS_SUB_TYPE_OR	= 0x01; // 001
	private static final int INS_SUB_TYPE_ADC	= 0x02; // 010
	private static final int INS_SUB_TYPE_SBB	= 0x03; // 011
	private static final int INS_SUB_TYPE_AND	= 0x04; // 100
	private static final int INS_SUB_TYPE_SUB	= 0x05; // 101
	private static final int INS_SUB_TYPE_XOR	= 0x06; // 110
	private static final int INS_SUB_TYPE_CMP	= 0x07; // 111
	
	// define Type B instruction codes
	private static final int INS_TYPE_B 		= 0x21; // 100001
	private static final int INS_TYPE_B_TEST	= 0x00; // 0
	private static final int INS_TYPE_B_XCHG	= 0x01; // 1
	
	// define Type C instruction codes
	private static final int INS_TYPE_C 		= 0x22; // 100010
	
	// define Type D instruction codes
	private static final int INS_TYPE_D 		= 0x23; // 100011
	private static final int INS_TYPE_D_MOV1	= 0x00; // 00
	private static final int INS_TYPE_D_LEA		= 0x01; // 01
	private static final int INS_TYPE_D_MOV2	= 0x02; // 10
	private static final int INS_TYPE_D_POP		= 0x03; // 11

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode( final Intel8086 cpu, final X86MemoryProxy proxy ) {
		// get the 16-bit instruction
		final int code16 = proxy.nextWord();
		
		// extract the instruction ID portion
		// code: ttrr rmmm iiii iisc (mask = 0000 0000 1111 1100)
		final int insCode = ( code16 & 0x00FC ) >> 2; 
		
		// handle the various instruction types
		switch( insCode ) {
			case INS_TYPE_A:	return decodeTypeA( cpu, proxy, code16 );
			case INS_TYPE_B:	return decodeTypeB( cpu, proxy, code16 );
			case INS_TYPE_C:	return decodeTypeC( cpu, proxy, code16 );
			case INS_TYPE_D:	return decodeTypeD( cpu, proxy, code16 );
			default: 			throw new IllegalStateException( String.format( "Unhandled byte code %04X", code16 ) );
		}
	}
	
	/**
	 * Decodes a Type A instructions (ADD, OR, ADC, SBB, AND, SUB, XOR, and CMP)
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link OpCode 80x86 opCode}  
	 */
	private OpCode decodeTypeA( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// ttjj jmmm iiii iikc 
		
		// extract the instruction sub-code
		// code: ..jj j... .... .... (mask = 0011 1000 0000 0000)
		final int subCode = ( code16 & 0x3800 ) >> 11;
		
		// extract the instruction sub-code
		// code: .... .... .... ..k. (mask = 0000 0000 0000 0010)
		final int groupCode = ( code16 & 0x0002 ) >> 1;
		
		// extract the memory class
		// code: .... .... .... ...c (mask = 0000 0000 0000 0001)
		final int memClass = ( code16 & 0x0001 );
		
		// determine the target and source
		final Operand target = lookupSecondaryOperand( cpu, proxy, code16 );
		final Operand source = ( groupCode == 1 ) ? nextSignedValue8( proxy ) : nextValue( proxy, memClass );
		
		// decode the instruction
		switch( subCode ) {
			case INS_SUB_TYPE_ADD:	return new ADD( target, source );
			case INS_SUB_TYPE_OR:	return new OR( target, source );
			case INS_SUB_TYPE_ADC:	return new ADC( target, source );
			case INS_SUB_TYPE_SBB:	return new SBB( target, source );
			case INS_SUB_TYPE_AND:	return new AND( target, source );
			case INS_SUB_TYPE_SUB:	return new SUB( target, source );
			case INS_SUB_TYPE_XOR:	return new XOR( target, source );
			case INS_SUB_TYPE_CMP:	return new CMP( target, source );
			default: 				throw new UnhandledByteCodeException( code16 );
		}
	}
	
	/**
	 * Decodes a Type B instructions (TEST and XCHG)
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link OpCode 80x86 opCode}  
	 */
	private OpCode decodeTypeB( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// ttrr rmmm iiii iijc 
		
		// extract the instruction sub-code
		// code: .... .... .... ..j. (mask = 0000 0000 0000 0010)
		final int subCode = ( code16 & 0x0002 ) >> 1;
		
		// lookup the register and reference
		final Operand[] operands = lookupOperands( cpu, proxy, code16, true, false );
		
		// capture the source and target
		final Operand target = operands[0];
		final Operand source = operands[1];
		
		// decode the instruction
		switch( subCode ) {
			case INS_TYPE_B_TEST:	return new TEST( target, source );
			case INS_TYPE_B_XCHG:	return new XCHG( target, source );
			default: 				throw new UnhandledByteCodeException( code16 );
		}
	}

	/**
	 * Decodes a Type C instructions (e.g. 'mov al,cl', 'mov [bx],al', 'mov ax,[bx]')
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link OpCode 80x86 opCode}  
	 */
	private OpCode decodeTypeC( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// ttrr rmmm iiii iisc 
		
		// lookup the primary (register) and secondary (memory reference) operands	
		final Operand[] operands = lookupOperands( cpu, proxy, code16 );
		
		// return the opCode
		return new MOV( operands[0], operands[1] );
	}
	
	/**
	 * Decodes a Type D instructions (MOV, LEA, and POP)
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link OpCode 80x86 opCode}  
	 */
	private OpCode decodeTypeD( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// ttrr rmmm iiii iijj 
		
		// extract the instruction sub-code
		// code: .... .... .... ..jj (mask = 0000 0000 0000 0011)
		final int subCode = ( code16 & 0x0003 );
		
		// decode the instruction
		switch( subCode ) {
			case INS_TYPE_D_MOV1:	return decodeMOV( cpu, proxy, code16 );
			case INS_TYPE_D_MOV2:	return decodeMOV( cpu, proxy, code16 );
			case INS_TYPE_D_LEA:	return decodeLEA( cpu, proxy, code16 );
			case INS_TYPE_D_POP:	return decodePOP( cpu, proxy, code16 );
			default: 				throw new UnhandledByteCodeException( code16 );
		}
	}
	
	/**
	 * Decodes a Load Executive Address (LEA) instruction
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link LEA Load Executive Address} instruction  
	 */
	private LEA decodeLEA( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// get the compsite (element & reference) code
		//	code16: tt.. .mmm .... .... (mask = 1100 0111 0000 0000)
		final int compCode = ( code16 & 0xC700 ) >> 8;
		
		// lookup the primary operand (register)
		final X86Register dst = lookupRegister( cpu, proxy, code16 );
			
		// lookup the secondary operand (memory reference)	
		final MemoryReference src = lookupReferencedAddress( cpu, proxy, compCode );
		
		// return the LEA instruction
		return new LEA( dst, src );
	}
	
	/**
	 * Decodes a MOV instruction
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link MOV MOV} instruction  
	 */
	private MOV decodeMOV( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// lookup the primary and secondary operand
		final Operand[] operands = lookupOperands( cpu, proxy, code16, true, true );
		
		// return the MOV instruction
		return new MOV( operands[0], operands[1] );
	}

	/**
	 * Decodes a POP instruction
	 * @param cpu the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction
	 * @return an {@link POP POP} instruction  
	 */
	private POP decodePOP( final Intel8086 cpu, final X86MemoryProxy proxy, final int code16 ) {
		// lookup the primary operand
		final Operand operand = lookupSecondaryOperand( cpu, proxy, code16 );
		
		// return the POP instruction
		return new POP( operand );
	}
	
	
}