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
 * Processes instruction codes between 90h and 9Fh (e.g. "CBW")
 * ---------------------------------------------------------------------------
 * instruction			code 			iiii jjj k
 * --------------------------------------------------------------------------- 
 * xchg ax,ax | nop		90				1001 000 0
 * xchg cx,ax			91				1001 000 1
 * xchg dx,ax			92				1001 001 0
 * xchg bx,ax			93				1001 001 1
 * xchg sp,ax			94				1001 010 0
 * xchg bp,ax			95				1001 010 1
 * xchg si,ax			96				1001 011 0
 * xchg di,ax			97				1001 011 1
 * cbw					98				1001 100 0
 * cwd					99				1001 100 1
 * call nnnn:nnnn		9A				1001 101 0 nnnn nnnn
 * wait					9B				1001 101 1
 * pushf				9C				1001 110 0
 * popf					9D				1001 110 1
 * sahf					9E				1001 111 0
 * lahf					9F				1001 111 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder90 implements InstructionEncoder {
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES =
		Arrays.asList( new String[] { 
			"NOP", "CBW", "CWD", "WAIT", 
			"PUSHF", "POPF", "SAHF", "LAHF"
		} );
	
	// define the instruction codes
	private static final List<Integer> INSTRUCTION_CODES = 
		Arrays.asList( new Integer[] { 
			0x90, 0x98, 0x99, 0x9B, 
			0x9C, 0x9D, 0x9E, 0x9F
	} );

	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86EncodeHelper#encode(ibmpc.machinecode.encoder.X86Encoder, ibmpc.machinecode.encoder.X86Instruction)
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