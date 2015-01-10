package ibmpc.devices.cpu.x86.decoder;

import static ibmpc.devices.cpu.operands.Operand.SIZE_32BIT;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupSecondaryOperand;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextValue;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.bitwise.NEG;
import ibmpc.devices.cpu.x86.opcodes.bitwise.NOT;
import ibmpc.devices.cpu.x86.opcodes.data.DB;
import ibmpc.devices.cpu.x86.opcodes.data.LOCK;
import ibmpc.devices.cpu.x86.opcodes.flags.CLC;
import ibmpc.devices.cpu.x86.opcodes.flags.CLD;
import ibmpc.devices.cpu.x86.opcodes.flags.CLI;
import ibmpc.devices.cpu.x86.opcodes.flags.CMC;
import ibmpc.devices.cpu.x86.opcodes.flags.STC;
import ibmpc.devices.cpu.x86.opcodes.flags.STD;
import ibmpc.devices.cpu.x86.opcodes.flags.STI;
import ibmpc.devices.cpu.x86.opcodes.flow.callret.CALL;
import ibmpc.devices.cpu.x86.opcodes.flow.jump.JMP;
import ibmpc.devices.cpu.x86.opcodes.math.DEC;
import ibmpc.devices.cpu.x86.opcodes.math.DIV;
import ibmpc.devices.cpu.x86.opcodes.math.IDIV;
import ibmpc.devices.cpu.x86.opcodes.math.IMUL;
import ibmpc.devices.cpu.x86.opcodes.math.INC;
import ibmpc.devices.cpu.x86.opcodes.math.MUL;
import ibmpc.devices.cpu.x86.opcodes.math.TEST;
import ibmpc.devices.cpu.x86.opcodes.stack.PUSH;
import ibmpc.devices.cpu.x86.opcodes.string.REPNZ;
import ibmpc.devices.cpu.x86.opcodes.string.REPZ;
import ibmpc.devices.cpu.x86.opcodes.system.HLT;
import ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes F0h thru FFh.
 * ---------------------------------------------------------------------------
 * instruction				code 	tt jjj mmm	iiii jjj k
 * --------------------------------------------------------------------------- 
 * lock						  F0				1111 000 0
 * (undefined)				  F1				1111 000 1
 * repnz					  F2				1111 001 0
 * repz						  F3				1111 001 1
 * hlt						  F4				1111 010 0
 * cmc						  F5				1111 010 1
 * (see below)				  F6				1111 011 0
 * (see below)				  F7				1111 011 1
 * clc						  F8				1111 100 0
 * stc						  F9				1111 100 1
 * cli						  FA				1111 101 0
 * sti						  FB				1111 101 1
 * cld						  FC				1111 110 0
 * std						  FD				1111 110 1
 * (see below)				  FE				1111 111 0
 * (see below)				  FF				1111 111 1
 * 
 * ---------------------------------------------------------------------------
 * instruction				code 	tt jjj mmm	iiiiii x c  
 * ---------------------------------------------------------------------------
 * test bl,nn				C3F6	11 000 011	111101 1 0 nn
 * test bx,nnnn				C3F7	11 000 011	111101 1 1 nnnn
 * (undefined)				08F7	00 001 000	111101 1 1
 * not ax					D0F7	11 010 000	111101 1 1 
 * neg ax					D8F7	11 011 000	111101 1 1 
 * mul ax					E0F7	11 100 000	111101 1 1 
 * imul ax					E8F7	11 101 000	111101 1 1 
 * div ax					F0F7	11 110 000	111101 1 1 
 * idiv ax					F8F7	11 111 000	111101 1 1  
 * 
 * inc byte ptr [bx]		07FE	00 000 111 	111111 1 0 
 * inc word ptr [bx]		07FF	00 000 111 	111111 1 1 
 * dec word ptr [bx]		0FFF	00 001 111	111111 1 1  
 * call [bx]				17FF	00 010 111	111111 1 1 
 * call far [bx]			1FFF	00 011 111	111111 1 1 
 * jmp [bx]					27FF	00 100 111	111111 1 1 
 * jmp far [bx]				2FFF	00 101 111	111111 1 1
 * push [bx]				37FF	00 110 111	111111 1 1
 * (undefined)				3FFF	00 111 111	111111 1 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class DecoderF0 implements Decoder {
	// define the instruction type A codes
	private static final int INS_TYPE_A 			= 0x3D; // 111101
	private static final int INS_TYPE_A_TEST 		= 0x00; // 000 
	private static final int INS_TYPE_A_NOT 		= 0x02; // 010 
	private static final int INS_TYPE_A_NEG 		= 0x03; // 011 
	private static final int INS_TYPE_A_MUL 		= 0x04; // 100 
	private static final int INS_TYPE_A_IMUL 		= 0x05; // 101 
	private static final int INS_TYPE_A_DIV 		= 0x06; // 110 
	private static final int INS_TYPE_A_IDIV 		= 0x07; // 111 
	
	// define the instruction type B codes
	private static final int INS_TYPE_B 			= 0x3F; // 111111
	private static final int INS_TYPE_B_INC 		= 0x00; // 000 
	private static final int INS_TYPE_B_DEC 		= 0x01; // 001 
	private static final int INS_TYPE_B_CALL		= 0x02; // 010 
	private static final int INS_TYPE_B_CALL_FAR	= 0x03; // 011 
	private static final int INS_TYPE_B_JMP			= 0x04; // 100
	private static final int INS_TYPE_B_JMP_FAR		= 0x05; // 101
	private static final int INS_TYPE_B_PUSH		= 0x06; // 110 
	
	// internal fields
	private final DecodeProcessor processor;
	
	/**
	 * Creates a new decoder instance
	 * @param processor the given {@link DecodeProcessor decode processor} instance
	 */
	public DecoderF0( final DecodeProcessor processor ) {
		this.processor = processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode( final Intel80x86 cpu, final X86MemoryProxy proxy ) {
		// peek at the next word
		final int code8  = proxy.nextByte();
		
		// evaluate the code
		switch( code8 ) {
			// LOCK
			case 0xF0:	return LOCK.getInstance();
			// (undefined)
			case 0xF1:	return new DB( code8 );
			// REPNZ
			case 0xF2: 	return new REPNZ( processor.decodeNext() );
			// REPZ
			case 0xF3: 	return new REPZ( processor.decodeNext() );
			// HLT
			case 0xF4: 	return HLT.getInstance();
			// CMC
			case 0xF5: 	return CMC.getInstance();
			// CLC
			case 0xF8: 	return CLC.getInstance();
			// STC
			case 0xF9: 	return STC.getInstance();
			// CLI
			case 0xFA: 	return CLI.getInstance();
			// STI
			case 0xFB:	return STI.getInstance();
			// CLD
			case 0xFC: 	return CLD.getInstance();
			// STD
			case 0xFD: 	return STD.getInstance();
			// complex code?
			default:	return decodeComplexCode( cpu, proxy, code8 );
		}
	}
	
	/**
	 * Decodes a complex F6, F7, FE, or FF instruction
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code8 the given 8-bit instruction code
	 * @return an {@link OpCode opCode}
	 */
	private OpCode decodeComplexCode( final Intel80x86 cpu, final X86MemoryProxy proxy, final int code8 ) {
		// get the 16-bit code
		// code: ttjj jmmm iiii iixc  
		final int code16 = proxy.nextWord( code8 );
		
		// extract the instruction subcode
		// code: ..jj j... .... .... (mask = 0011 1000 0000 0000)  
		final int subCode = ( code16 & 0x3800 ) >> 11; 
		
		// extract the instruction code
		// code: .... .... iiii ii.. (mask = 0000 0000 1111 1100)  
		final int insCode = ( code16 & 0x00FC ) >> 2;
				
		// decode the instruction
		switch( insCode ) {
			case INS_TYPE_A: return decodeTypeA( cpu, proxy, code16, subCode );
			case INS_TYPE_B: return decodeTypeB( cpu, proxy, code16, subCode );
			default:		 throw new UnhandledByteCodeException( code16 );
		}
	}
	
	/** 
	 * Decodes the given Type A (TEST,NOT,NEG,MUL,IMUL,DIV,IDIV) instruction
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction code
	 * @param subCode the given 3-bit instruction sub-code
	 * @return the {@link OpCode decoded instruction}
	 */
	private OpCode decodeTypeA( final Intel80x86 cpu, final X86MemoryProxy proxy, final int code16, final int subCode ) {		
		// extract the memory class 
		// code: .... .... .... ...c (mask = 0000 0000 0000 0001)
		final int memClass = ( code16 & 0x0001 );
		
		// lookup the secondary operand
		final Operand operand = lookupSecondaryOperand( cpu, proxy, code16 );
		
		// decode the instruction
		switch( subCode ) {
			case INS_TYPE_A_TEST:	return new TEST( operand, nextValue( proxy, memClass ) );
			case INS_TYPE_A_NOT:	return new NOT( operand );
			case INS_TYPE_A_NEG:	return new NEG( operand );
			case INS_TYPE_A_MUL:	return new MUL( operand );
			case INS_TYPE_A_IMUL:	return new IMUL( operand );
			case INS_TYPE_A_DIV:	return new DIV( operand );
			case INS_TYPE_A_IDIV:	return new IDIV( operand );
			default:				throw new UnhandledByteCodeException( code16 );
		}
	}
	
	/** 
	 * Decodes the given Type B (CALL,JMP,PUSH,INC,DEC) instruction
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
	 * @param code16 the given 16-bit instruction code
	 * @param subCode the given 3-bit instruction sub-code
	 * @return the {@link OpCode decoded instruction}
	 */
	private OpCode decodeTypeB( final Intel80x86 cpu, final X86MemoryProxy proxy, int code16, final int subCode ) {
		// decode the instruction
		switch( subCode ) {
			case INS_TYPE_B_CALL:		return new CALL( lookupSecondaryOperand( cpu, proxy, code16 ) ); 
			case INS_TYPE_B_CALL_FAR:	return new CALL( lookupSecondaryOperand( cpu, proxy, code16, SIZE_32BIT ) ); 
			case INS_TYPE_B_JMP:		return new JMP( lookupSecondaryOperand( cpu, proxy, code16 ) ); 
			case INS_TYPE_B_JMP_FAR:	return new JMP( lookupSecondaryOperand( cpu, proxy, code16, SIZE_32BIT ) ); 
			case INS_TYPE_B_PUSH:		return new PUSH( lookupSecondaryOperand( cpu, proxy, code16 ) );
			case INS_TYPE_B_INC:		return new INC( lookupSecondaryOperand( cpu, proxy, code16 ) );
			case INS_TYPE_B_DEC:		return new DEC( lookupSecondaryOperand( cpu, proxy, code16 ) );
			default:					throw new UnhandledByteCodeException( code16 );
		}
	}

}
