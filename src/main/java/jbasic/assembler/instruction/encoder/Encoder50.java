package jbasic.assembler.instruction.encoder;


import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.instruction.element.X86DataElement;
import jbasic.assembler.instruction.element.registers.X86RegisterRef;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes between 50h and 5Fh
 *	---------------------------------------------------------------------------
 *	type	size		description 			comments
 *	---------------------------------------------------------------------------
 *	i		5-bit 		instruction type  		push=01010, pop=01011
 *	r		3-bit		register index			ax=000
 * 
 *  Instruction code layout
 *  -----------------------
 *  7654 3210 (8 bits)
 *  iiii irrr   
 * 
 * ---------------------------------------------------------------------------
 * instruction			code 	iiiii rrr 
 * ---------------------------------------------------------------------------
 * push ax				50		01010 000
 * push cx				51		01010 001
 * push dx				52		01010 010
 * push bx				53		01010 011
 * push sp				54		01010 100
 * push bp				55		01010 101
 * push si				56		01010 110
 * push di				57		01010 111
 * pop ax				58		01011 000
 * pop cx				59		01011 001
 * pop dx				5A		01011 010
 * pop bx				5B		01011 011
 * pop sp				5C		01011 100
 * pop bp				5D		01011 101
 * pop si				5E		01011 110
 * pop di				5F		01011 111
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder50 implements InstructionEncoder {
	// define the instruction codes
	private static final int PUSH_CODE	= 0x0A;	// 01010
	private static final int POP_CODE	= 0x0B; // 01011
	
	// define the instruction mnemonics
	private static final String PUSH_NAME = "PUSH";
	private static final String POP_NAME  = "POP";
	private static final List<String> INSTRUCTION_NAMES = 
		Arrays.asList( new String[] {
				PUSH_NAME, POP_NAME
		} );
	
	// define the instruction mnemonic to instruction code mapping
	private static final Map<String,Integer> INSTRUCTION_CODES = new HashMap<String, Integer>( 2 );
	static {
		INSTRUCTION_CODES.put( PUSH_NAME, PUSH_CODE );
		INSTRUCTION_CODES.put( POP_NAME, POP_CODE );
	}

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
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
	 * @return true, if the instruction is a PUSH or POP instruction
	 * with a general purpose 16-bit register as it's only parameter.
	 */
	private boolean isEncodable( final X86Instruction instruction ) {
		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// must be a PUSH or POP instruction		
		// and elem #1 must a 16-bit general purpose register
		return INSTRUCTION_CODES.containsKey( instruction.getName() ) && 
				( elems.length == 1  ) && 
				elems[0].isRegister() &&
				( ((X86RegisterRef)elems[0]).is16BitGeneralPurposeRegister() );
	}

}
