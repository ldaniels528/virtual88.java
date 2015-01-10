package ibmpc.instruction.encoder;

import static ibmpc.instruction.element.registers.X86RegisterReferences.CL;
import static ibmpc.instruction.element.values.X86ByteValue.ONE;
import static ibmpc.instruction.encoder.X86AssemblyUtil.checkParameterCount;
import static ibmpc.instruction.encoder.X86AssemblyUtil.createInstructionMapping;
import static ibmpc.instruction.encoder.X86AssemblyUtil.is8Bit;
import static ibmpc.instruction.encoder.X86AssemblyUtil.isGeneralPurposeRegister;

import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ibmpc.instruction.InstructionEncoder;
import ibmpc.instruction.X86Instruction;
import ibmpc.instruction.element.X86DataElement;
import ibmpc.instruction.element.registers.X86RegisterRef;
import ibmpc.instruction.exception.X86InvalidRegisterUsageException;
import ibmpc.instruction.exception.X86MalformedInstructionException;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction code between D0h and DFh 
 * --------------------------------------------------------------------------- 
 * Type A
 * 
 * 		----------------------------------------------------------
 * 		type	size (bits)	description 				comments
 *		----------------------------------------------------------
 * 		t		2			register/memory type		register=11b
 * 		j		3			instruction sub-code	
 * 		r		3			register/memory reference	
 * 		i		5	 		instruction type  	
 * 		k		1			function Code		
 * 		s		1			source type					'cl'=1, '1'=0
 * 		c		1	 		memory class 	 			8-bit=0, 16-bit=1	
 * 		d		16/32		offset 
 *		----------------------------------------------------------
 * 
 * instruction 					code	tt jjj rrr	iiiii k s c dddd
 * ---------------------------------------------------------------------------
 * shl byte ptr [bx],1			27D0	00 100 111	11010 0 0 0 
 * rol al,1						C0D0	11 000 000	11010 0 0 0 
 * rol cl,1						C1D0	11 000 001	11010 0 0 0 
 * ror al,1						C8D0	11 001 000	11010 0 0 0 
 * ror cl,1						C9D0	11 001 001	11010 0 0 0 
 * shl al,1						E0D0	11 100 000	11010 0 0 0 
 * shl cl,1						E1D0	11 100 001	11010 0 0 0 
 * shr al,1						E8D0	11 101 000	11010 0 0 0 
 * shr cl,1						E9D0	11 101 001	11010 0 0 0 
 * sar cl,1						F9D0	11 111 001	11010 0 0 0 
 * sar word ptr [si],1			3CD1	00 111 100	11010 0 0 1 
 * shl byte ptr [bx],cl			27D2	00 100 111	11010 0 1 0 
 * shl byte ptr [si+nnnn],cl	A4D2	10 100 100	11010 0 1 0 nnnn
 * rcl al,cl					D0D2	11 010 000	11010 0 1 0 
 * rcl cl,cl					D1D2	11 010 001	11010 0 1 0 
 * rcr al,cl					D8D2	11 011 000	11010 0 1 0 
 * rcr cl,cl					D9D2	11 011 001	11010 0 1 0 
 * shl al,cl					E0D2	11 100 000	11010 0 1 0 
 * shl cl,cl					E1D2	11 100 001	11010 0 1 0 
 * shl ax,cl					E0D3	11 100 000	11010 0 1 1 
 * 
 * --------------------------------------------------------------------------- 
 * Type B
 * instruction 					code	tt rrr mmm 	iiiii j kk
 * ---------------------------------------------------------------------------
 * aam 							0AD4	00 001 010	11010 1 00 	
 * aad							0AD5	00 001 010	11010 1 01
 * (undefined)					  D6				11010 1 10
 * xlat							  D7				11010 1 11
 * 
 * --------------------------------------------------------------------------- 
 * Type C
 * instruction 					code	tt jjj rrr	iiiii c kk dddd
 * ---------------------------------------------------------------------------
 * fadd  dword ptr [bx]			07D8	00 000 111	11011 0 00
 * fmul  dword ptr [bx]			0FD8	00 001 111	11011 0 00
 * fsub  dword ptr [bx]			27D8	00 100 111	11011 0 00
 * fdiv  dword ptr [bx]			37D8	00 110 111	11011 0 00
 * 
 * fld   dword ptr [bx]			07D9	00 000 111	11011 0 01
 * fst   dword ptr [bx]			17D9	00 010 111	11011 0 01
 * fldenv [bx]					27D9	00 101 111	11011 0 01
 * fstenv [bx]					37D9	00 110 111	11011 0 01
 * 
 * fiadd dword ptr [bx]			07DA	00 000 111	11011 0 10
 * fimul dword ptr [bx]			0FDA	00 001 111	11011 0 10
 * fisub dword ptr [bx]			27DA	00 100 111	11011 0 10
 * fidiv dword ptr [bx]			37DA	00 110 111	11011 0 10
 * 
 * fild  dword ptr [bx]			07DB	00 000 111	11011 0 11
 * fist  dword ptr [bx]			17DB	00 010 111	11011 0 11
 * esc   1C,[bx]tbyte ptr [bx]	27DB	00 101 111	11011 0 11
 * esc   1E,[bx]tbyte ptr [bx]	37DB	00 110 111	11011 0 11
 * 
 * fadd  qword ptr [bx]			07DC	00 000 111	11011 1 00
 * fmul  qword ptr [bx]			0FDC	00 001 111	11011 1 00
 * fsub  qword ptr [bx]			27DC	00 100 111	11011 1 00
 * fdiv  qword ptr [bx]			37DC	00 110 111	11011 1 00
 * 
 * fld   qword ptr [bx]			07DD	00 000 111	11011 1 01
 * fst   qword ptr [bx]			17DD	00 010 111	11011 1 01
 * frstor [bx]					27DD	00 101 111	11011 1 01
 * fsave  [bx]					37DD	00 110 111	11011 1 01
 * 
 * fiadd  word ptr [bx]			07DE 	00 000 111	11011 1 10 
 * fimul  word ptr [bx]			0FDE 	00 001 111	11011 1 10
 * fisub  word ptr [bx]			27DE	00 100 111	11011 1 10
 * fidiv  word ptr [bx]			37DE	00 110 111	11011 1 10
 *
 * fild   word ptr [bx]			07DF	00 000 111	11011 1 11
 * fist   word ptr [bx]			17DF	00 010 111	11011 1 11
 * fbld  tbyte ptr [bx]			27DF	00 101 111	11011 1 11
 * fbstp tbyte ptr [bx]			37DF	00 110 111	11011 1 11
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class EncoderD0 implements InstructionEncoder {
	// define the instruction code
	private static final int INSTRUCTION_CODE_A = 0x34; // 110100
	private static final int INSTRUCTION_CODE_B = 0x35; // 110101
	private static final int INSTRUCTION_CODE_C = 0x36; // 110110
	
	// define the instruction sub codes
	private static final int ROL = 0; // 000b
	private static final int ROR = 1; // 001b
	private static final int RCL = 2; // 010b
	private static final int RCR = 3; // 011b
	private static final int SHL = 4; // 100b
	private static final int SHR = 5; // 101b
	private static final int SAL = 6; // 110b
	private static final int SAR = 7; // 111b
	
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES_A = 
		Arrays.asList( new String[] {
				"RCL", "RCR", "ROL", "ROR", "SAL", "SAR", "SHL", "SHR"
		} );
	
	// define the instruction nmemonics
	private static final List<Integer> INSTRUCTION_CODES_A = 
		Arrays.asList( new Integer[] {
				RCL, RCR, ROL, ROR, SAL, SAR, SHL, SHR
		} );
	
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES_B = 
		Arrays.asList( new String[] {
				"AAM", "AAD", "XLAT"
		} );
	
	// define the instruction nmemonics
	private static final List<Integer> INSTRUCTION_CODES_B = 
		Arrays.asList( new Integer[] {
				0x0AD4, 0x0AD5, 0xD7
		} );
	
	// static fields
	private static final Map<String,Integer> INSTRUCTION_MAPPING_A = 
		createInstructionMapping( INSTRUCTION_NAMES_A, INSTRUCTION_CODES_A );

	/* (non-Javadoc)
	 * @see ibmpc.compiler.neo.encoders.X86EncodeHelper#encode(ibmpc.compiler.encoder.X86Encoder, ibmpc.compiler.encoder.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer encoder, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// make sure it's a handled instruction
		if( !INSTRUCTION_MAPPING_A.containsKey( instruction.getName() ) ) return false;
		
		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// check the parameter count
		checkParameterCount( elems, 2 );
		
		// lookup the instruction
		final int insSubCode		= INSTRUCTION_MAPPING_A.get( instruction.getName() );
		final X86DataElement elem1	= elems[0];
	 	final X86DataElement elem2	= elems[1];
		
		// Instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// ttjj jrrr iiii iasc 
		// -----------------------------
		
		// id the 1st data element is a register, it must be a general purpose
		if( elem1.isRegister() && !isGeneralPurposeRegister( elem1 ) ) {
			throw new X86InvalidRegisterUsageException( (X86RegisterRef)elem1 );
		}
		
		// the 2nd data element must be either '1' or 'cl'
		if( !isOneOrCL( elem2 ) ) {
			throw new X86MalformedInstructionException( "The source element must be 1 or CL" );
		}
		
		// create the 16-bit "Type C1" instruction
		int code16;
		
		// set the register/reference element type (11b=register, 00b/01b/10b=reference)
		// code: tt.. .... .... .... (02/16 bits)
		code16 = ( ( elem1.isRegister() ? 0x02 : 0 ) << 14 );
		
		// set the instruction sub code bits 
		// code: ttjj j... .... .... (05/16 bits)
		code16 |= ( insSubCode << 11 );
		
		// set the target register index bits
		// code: ttjj jrrr .... .... (08/16 bits)
		code16 |= ( elem1.getSequence() << 8 );
		
		// set the instruction code bits
		// code: ttjj jrrr iiii ii.. (14/16 bits)
		code16 |= ( INSTRUCTION_CODE_A << 2 ); 
		
		// set the source type identifier bit (0='1', 1='cl')
		// code: jj jrrr iiii iis. (15/16 bits)
		code16 |= ( ( elem2.equals( CL ) ? 1 : 0 ) << 1 );
		
		// set the memory class bit (0=8-bit, 1=16-bit) 
		// code: rrjj jrrr iiii iisc (16/16 bits)
		code16 |= ( is8Bit( elem1 ) ? 0 : 1 );
		
		// encode the instruction
		encoder.setWord( code16 );
		return true;
	}

	/**
	 * Validates the given data element (must be '1' or 'CL')
	 * @param elem the given {@link X86DataElement data element}
	 * @return true, if the data element is valid for this instruction
	 */
	private boolean isOneOrCL( final X86DataElement elem ) {
		// is it a numeric value?
		if( elem.isValue() ) {
			return elem.equals( ONE );
		}
		
		// is it a register?
		else if( elem.isRegister() ) {
			// if the register is CL, it's valid
			return elem.equals( CL );
		}
		
		return false;
	}

}
