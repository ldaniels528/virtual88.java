package jbasic.assembler.instruction.encoder;

import static jbasic.assembler.instruction.encoder.X86AssemblyUtil.checkParameterCount;

import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;

import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * <pre>
 * Instruction Processor for instruction codes A0h thru AFh, including:
 * LOCK		HLT		REPZ	REPNZ
 * CLC		STC		CMC
 * CLI		STI 	CLD		STD
 * 
 * ---------------------------------------------------------------------------
 * instruction				code 	tt rrr mmm	iiii jjj k
 * --------------------------------------------------------------------------- 
 * lock						  F0				1111 000 0
 * (undefined)				  F1				1111 000 1
 * repnz					  F2				1111 001 0
 * repz						  F3				1111 001 1
 * hlt						  F4				1111 010 0
 * cmc						  F5				1111 010 1
 * clc						  F8				1111 100 0
 * stc						  F9				1111 100 1
 * cli						  FA				1111 101 0
 * sti						  FB				1111 101 1
 * cld						  FC				1111 110 0
 * std						  FD				1111 110 1
 * 
 * ---------------------------------------------------------------------------
 * instruction				code 	tt rrr mmm	iiiiii x c  
 * ---------------------------------------------------------------------------
 * mul byte ptr [bx+si+nn]	60F6nn	01 100 000	111101 1 0 nn 
 * mul word ptr [bx]		27F7	00 100 111	111101 1 1
 * 
 * ---------------------------------------------------------------------------
 * instruction				code 	tt jjj rrr	iiiiii x c  
 * ---------------------------------------------------------------------------
 * not ax					D0F7	11 010 000	111101 1 1 
 * neg ax					D8F7	11 011 000	111101 1 1 
 * mul ax					E0F7	11 100 000	111101 1 1 
 * imul ax					E8F7	11 101 000	111101 1 1 
 * div ax					F0F7	11 110 000	111101 1 1 
 * idiv ax					F8F7	11 111 000	111101 1 1  
 * 
 * call [bx]				17FF	00 010 111	111111 1 1 
 * jmp [bx]					27FF	00 100 111	111111 1 1 
 * push [bx]				37FF	00 110 111	111111 1 1
 * inc ax					C0FF	11 000 000 	111111 1 1 
 * dec ax					C8FF	11 001 000	111111 1 1  
 * call ax					D0FF	11 010 000	111111 1 1 
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class EncoderF0 implements InstructionEncoder {
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES =
		Arrays.asList( new String[] {
			"LOCK", "REPNZ", "REPZ", "HLT", "CMC", 
			"CLC", "STC", "CLI", "STI", "CLD", "STD"
		} );
	
	// define the instruction codes
	private static final List<Integer> INSTRUCTION_CODES = 
		Arrays.asList( new Integer[] {
			0xF0, 0xF2, 0xF3, 0xF4, 0xF5, 
			0xF8, 0xF9, 0xFA, 0xFB, 0xFC, 0xFD
	} );

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// make sure it's a handled instruction
		if( !INSTRUCTION_NAMES.contains( instruction.getName() ) ) return false;
		
		// check the parameter count
		checkParameterCount( instruction.getParameters(), 0 );
		
		// get the instruction ID
		final int instructionID = INSTRUCTION_NAMES.indexOf( instruction.getName() );
		
		// lookup the instruction
		final int instructionCode = INSTRUCTION_CODES.get( instructionID );
		
		// encode the instruction
		codebuf.setByte( instructionCode );
		return true;
	}

}
