package org.ldaniels528.javapc.ibmpc.compiler.encoder;


import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ldaniels528.javapc.ibmpc.compiler.InstructionEncoder;
import org.ldaniels528.javapc.ibmpc.compiler.X86Instruction;
import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;
import org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterRef;
import org.ldaniels528.javapc.ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes between 40h and 4Fh 
 *	---------------------------------------------------------------------------
 *	type	size		description 			comments
 *	---------------------------------------------------------------------------
 *	i		5-bit 		instruction type  		inc=01000,dec=01001
 *	r		3-bit		register index			ax=000b, cx=001b
 * 
 *  Instruction code layout
 *  -----------------------
 *  7654 3210 (8 bits)
 *  iiii irrr 
 *  
 * ---------------------------------------------------------------------------
 * instruction			code 	iiiii rrr 
 * ---------------------------------------------------------------------------
 * inc ax				40		01000 000
 * inc cx				41		01000 001
 * inc dx				42		01000 010
 * inc bx				43		01000 011
 * inc sp				44		01000 100
 * inc bp				45		01000 101
 * inc si				46		01000 110
 * inc di				47		01000 111
 * dec ax				48		01001 000
 * dec cx				49		01001 001
 * dec dx				4A		01001 010
 * dec bx				4B		01001 011
 * dec sp				4C		01001 100
 * dec bp				4D		01001 101
 * dec si				4E		01001 110
 * dec di				4F		01001 111
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder40 implements InstructionEncoder {
	// define the instruction codes
	private static final int INC_CODE	= 0x08;	// 01000
	private static final int DEC_CODE	= 0x09; // 01001
	
	// define the instruction mnemonics
	private static final String INC_NAME = "INC";
	private static final String DEC_NAME = "DEC";
	private static final List<String> INSTRUCTION_NAMES = 
		Arrays.asList( new String[] {
				INC_NAME, DEC_NAME
		} );
	
	// define the instruction mnemonic to instruction code mapping
	private static final Map<String,Integer> INSTRUCTION_CODES = new HashMap<String, Integer>( 2 );
	static {
		INSTRUCTION_CODES.put( INC_NAME, INC_CODE );
		INSTRUCTION_CODES.put( DEC_NAME, DEC_CODE );
	}

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.x86.instruction.InstructionEncoder#assemble(org.ldaniels528.javapc.ibmpc.x86.util.X86CodeBuffer, org.ldaniels528.javapc.ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// determine whether the instruction is encodable
		if( !isEncodable( instruction ) ) {
			return false;
		}

		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// get the instruction and register codes
		final int insCode = INSTRUCTION_CODES.get( instruction.getName() );
		final int regCode = elems[0].getSequence(); 
		
		//  Instruction code layout
		//  -----------------------
		//  7654 3210 (8 bits)
		//  iiii irrr 
		
		// create the 8-bit 80x86 byte code
		final int code = ( insCode << 3 ) | regCode;
				
		// encode the instruction
		codebuf.setByte( code );
		return true;
	}
	
	/**
	 * Determines whether the given instruction can be encoded
	 * as 80x86 machine code.
	 * @param instruction the given {@link X86Instruction instruction}
	 * @return true, if the instruction is a INC or DEC instruction
	 * with a general purpose 16-bit register as it's only parameter.
	 */
	private boolean isEncodable( final X86Instruction instruction ) {
		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// must be a INC or DEC instruction		
		// and elem #1 must a 16-bit general purpose register
		return INSTRUCTION_NAMES.contains( instruction.getName() ) && 
				( elems.length == 1  ) && 
				elems[0].isRegister() &&
				( ((X86RegisterRef)elems[0]).is16BitGeneralPurposeRegister() );
	}

}