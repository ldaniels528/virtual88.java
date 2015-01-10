package ibmpc.instruction.encoder;

import static ibmpc.devices.memory.X86MemoryUtil.getHighByte;
import static ibmpc.devices.memory.X86MemoryUtil.getLowByte;
import static ibmpc.instruction.encoder.X86AssemblyUtil.checkParameterCount;

import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;

import ibmpc.instruction.InstructionEncoder;
import ibmpc.instruction.X86Instruction;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Instruction Processor for instruction codes A0 thru AF, including:
 * MOV	
 * 		MOVSB	MOVSW
 * 		CMPSB	CMPSW
 * TEST
 * 		STOSB	STOSW
 * 		LODSB	LODSW 
 * 		SCASB	SCASW
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
public class EncoderA0 implements InstructionEncoder {	
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES =
		Arrays.asList( new String[] {
			"MOVSB", "MOVSW", "CMPSB", "CMPSW", 
			"STOSB", "STOSW", "LODSB", "LODSW", "SCASB", "SCASW"  
		} );
	
	// define the instruction codes
	private static final List<Integer> INSTRUCTION_CODES = 
		Arrays.asList( new Integer[] {
			0xA4, 0xA5, 0xA6, 0xA7, 
			0xAA, 0xAB, 0xAC, 0xAD, 0xAE, 0xAF
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
		if( instructionCode < 256 ) {
			codebuf.setByte( instructionCode );
			return true;
		}
		else {
			codebuf.setByte( getLowByte( instructionCode ) );
			codebuf.setByte( getHighByte( instructionCode ) );
			return true;
		}
	}
	
}
