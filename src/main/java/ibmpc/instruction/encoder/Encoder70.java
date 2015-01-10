package ibmpc.instruction.encoder;


import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;

import ibmpc.instruction.InstructionEncoder;
import ibmpc.instruction.X86Instruction;
import ibmpc.instruction.element.X86DataElement;
import ibmpc.instruction.element.registers.X86RegisterRef;
import ibmpc.instruction.element.values.X86NumericValue;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes between 70h and 7Fh
  *	---------------------------------------------------------------------------
 *	type	size		description 			comments
 *	---------------------------------------------------------------------------
 *	i		4-bit 		instruction type  		inc=01000,dec=01001
 *	z		4-bit		unknown					ax=000b, cx=001b
 * 
 *  Instruction code layout
 *  -----------------------
 *  7654 3210 (8 bits)
 *  iiii irrr 
 *  
 * ---------------------------------------------------------------------------
 * instruction			code 	iiii zzzz 
 * ---------------------------------------------------------------------------
 * jo  nnnn				70		0111 0000
 * jno nnnn				71		0111 0001
 * jc  nnnn				72		0111 0010
 * jnc nnnn				73		0111 0011
 * jz  nnnn				74		0111 0100
 * jnz nnnn				75		0111 0101
 * jbe nnnn				76		0111 0110
 * ja  nnnn				77		0111 0111
 * js  nnnn				78		0111 1000
 * jns nnnn				79		0111 1001
 * jpe nnnn				7A		0111 1010
 * jpo nnnn				7B		0111 1011
 * jl  nnnn				7C		0111 1100
 * jge nnnn				7D		0111 1101
 * jle nnnn				7E		0111 1110
 * jg  nnnn				7F		0111 1111
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder70 implements InstructionEncoder {	
	// define the instruction mnemonics for encoding
	private static final List<String> ENCODE_INSTRUCTION_MNEMONICS = 
		Arrays.asList( new String[] {
			"JO",  "JNO", "JC",  "JB", "JNC", "JNB", "JE",  "JZ",
			"JNE", "JNZ", "JBE", "JA", "JS",  "JNS", "JPE", "JPO",
			"JL",  "JGE", "JLE", "JG"
		} );
	
	// define the conditional jump codes
	private static final List<Integer> ENCODE_INSTRUCTION_CODES = 
		Arrays.asList( new Integer[] {
			0x70, 0x71, 0x72, 0x72, 0x73, 0x73, 0x74, 0x74,
			0x75, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x7B,
			0x7C, 0x7D, 0x7E, 0x7F
		} );

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// determine whether the instruction is encodable
		if( !isEncodable( instruction ) ) {
			return false;
		}
		
		// get the instruction code
		final int index = ENCODE_INSTRUCTION_MNEMONICS.indexOf( instruction.getName() );
		final int code = ENCODE_INSTRUCTION_CODES.get( index );
		
		// get the offset code
		final X86DataElement[] elems = instruction.getParameters();
		final int offset = ((X86NumericValue)elems[0]).getValue();
		
		// encode the instruction
		codebuf.setByte( code );
		codebuf.setShortOffset( offset );
		return true;
	}

	/**
	 * Determines whether the given instruction can be encoded
	 * as 80x86 machine code.
	 * @param instruction the given {@link X86Instruction instruction}
	 * @return true, if the instruction is a PUSH or POP instruction
	 * with a general purpose 16-bit register as it's only parameter.
	 */
	private boolean isEncodable( final X86Instruction instruction ) {
		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// must be a PUSH or POP instruction		
		// and elem #1 must a 16-bit general purpose register
		return ENCODE_INSTRUCTION_MNEMONICS.contains( instruction.getName() ) && 
				( elems.length == 1  ) && 
				elems[0].isRegister() &&
				( ((X86RegisterRef)elems[0]).isValue() );
	}
	
}
