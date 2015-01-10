package ibmpc.compiler.encoder;

import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.MEM_CLASS_16BIT;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.MEM_CLASS_8BIT;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.SRC_CODE_PRIMARY;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.SRC_CODE_SECONDARY;
import static ibmpc.compiler.encoder.X86AssemblyUtil.generateReferenceCode;
import static ibmpc.compiler.encoder.X86AssemblyUtil.getElementOrder;
import static ibmpc.compiler.encoder.X86AssemblyUtil.is8Bit;
import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;

import ibmpc.compiler.InstructionEncoder;
import ibmpc.compiler.X86Instruction;
import ibmpc.compiler.element.X86DataElement;
import ibmpc.compiler.element.registers.X86RegisterReferences;
import ibmpc.compiler.exception.X86InvalidNumberOfParametersException;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Processes 8/16-bit dual parameter mathematic instructions including: 
 * ADC (add carry)
 * ADD (add)
 * AND (Bitwise AND)
 * CMP (compare)
 * OR  (Bitwise OR)
 * SBB (substract barrow)
 * SUB (substract)
 * XOR (Bitwise Exclusive OR)
 *  
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
 * instruction				code 		tt rrr mmm	iiiiii s c dddd
 * ------------------------------------------------------------------"
 * add	[bx],al				0700		00 000 111 	000000 0 0
 * add	[bx],ax				0701		00 000 111 	000000 0 1
 * add	al, [bx]			0702		00 000 111	000000 1 0 
 * add	ax, [bx]			0703		00 000 111	000000 1 1 
 * add	al,nn				  04					000001 0 0 nn
 * add	ax,nnnn				  05					000001 0 1 nnnn
 * push es					  06					000001 1 0
 * pop es					  07					000001 1 1
 * or	[bx],al				0708		00 000 111 	000010 0 1 
 * or	[bx],ax				0709		00 000 111 	000010 0 1 
 * or	al, [bx]			070A		00 000 111 	000010 1 0 
 * or	ax, [bx]			070B		00 000 111 	000010 1 1 
 * or 	al,nn				  0C					000011 0 0 nn
 * or 	ax,nnnn				  0D					000011 0 1 nnnn
 * push cs					  0E					000011 1 0
 * pop cs					  0F					000011 1 1
 * </pre>
 * @see X86RegisterReferences
 * @author lawrence.daniels@gmail.com
 */
public class Encoder00 implements InstructionEncoder {
	// define the type A instruction code constants
	private static final int ADD_CODE_A 	= 0x00;	// 000000
	private static final int OR_CODE_A  	= 0x02; // 000010
	private static final int ADC_CODE_A 	= 0x04;	// 000100
	private static final int SBB_CODE_A 	= 0x06; // 000110
	private static final int AND_CODE_A 	= 0x08; // 001000
	private static final int SUB_CODE_A 	= 0x0A; // 001010
	private static final int XOR_CODE_A 	= 0x0C; // 001100
	private static final int CMP_CODE_A 	= 0x0E; // 001110

	// define the type B instruction code constants
	private static final int ADD_CODE_B 	= 0x01;	// 000001
	private static final int OR_CODE_B  	= 0x03; // 000011
	private static final int ADC_CODE_B 	= 0x05;	// 000101
	private static final int SBB_CODE_B 	= 0x07; // 000111
	private static final int AND_CODE_B 	= 0x09; // 001001
	private static final int SUB_CODE_B 	= 0x0B; // 001011
	private static final int XOR_CODE_B 	= 0x0D; // 001101
	private static final int CMP_CODE_B 	= 0x0F; // 001111
	
	// define the type C instruction code constants
	private static final int INS_CODE_C 	= 0x20;	// 100000
	private static final int ADD_CODE_C 	= 0x00;	// 000
	private static final int OR_CODE_C  	= 0x01; // 001
	private static final int ADC_CODE_C 	= 0x02;	// 010
	private static final int SBB_CODE_C 	= 0x03; // 011
	private static final int AND_CODE_C 	= 0x04; // 100
	private static final int SUB_CODE_C 	= 0x05; // 101
	private static final int XOR_CODE_C 	= 0x06; // 110
	private static final int CMP_CODE_C 	= 0x07; // 111
	
	
	// define the list of instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES = 
		Arrays.asList("ADD", "OR", "ADC", "SBB", "AND", "SUB", "XOR", "CMP");
	
	// define the list of instruction codes
	private static final List<Integer> INSTRUCTION_CODES = 
		Arrays.asList(ADD_CODE_A, OR_CODE_A, ADC_CODE_A, SBB_CODE_A, AND_CODE_A, SUB_CODE_A, XOR_CODE_A, CMP_CODE_A);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean assemble( final X86CodeBuffer encoder, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// make sure it's a handled instruction
		if( !INSTRUCTION_NAMES.contains( instruction.getName() ) ) return false;
		
		// make sure there are only two parameters
		final X86DataElement[] elems = instruction.getParameters();
		if( elems.length != 2 )
			throw new X86InvalidNumberOfParametersException( 2, elems.length );
		
		// lookup the instruction
		final int index	= INSTRUCTION_NAMES.indexOf( instruction.getName() );
		final int insCode = INSTRUCTION_CODES.get( index );
		
		// determine the primary and secondary data elements
		final int[] order	= getElementOrder( elems[0], elems[1] );
		final int primary	= order[0];
		final int secondary	= order[1];
		
		// define new elements
		final X86DataElement elem1 = elems[primary];
		final X86DataElement elem2 = elems[secondary];
		
		// determine whether a constant is being used as the source
		final boolean isConstant = elem2.isValue();
		
		// generate the primary to secondary element code
		final int elemcode = generateReferenceCode( primary, secondary );
		
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// mmrr rmmm iiii ivsc 
		// -----------------------------
		//	m -> [2 bit] memory address info 	upper 2-bits = 8/16-bit value
		//	r -> [3 bit] register info
		//	m -> [3 bit] memory address info 	lower 3-bits = type
		// 	i -> [6 bit] instruction type  
		//	s -> [1 bit] source type			(register=1, memoryAdress=0)
		//	c -> [1 bit] register class  		(8-bit=0, 16-bit=1)
		
		// build the 16-bit instruction
		int code16;
		
		// set the primary to secondary element code
		// code: mmrr rmmm .... .... (08/16 bits)
		code16 = ( isConstant ? 0 : ( elemcode << 8 ) );
		
		// set the instruction code identifier
		// code: mmrr rmmm iiii i... (13/16 bits)
		code16 |= ( insCode << 3 );
		
		// set the constant/non-constant bit
		// code: mmrr rmmm iiii iv.. (14/16 bits)
		code16 |= ( ( isConstant ? 1 : 0 ) << 2 );
		
		// set the source type identifier 
		// code: mmrr rmmm iiii ivs. (15/16 bits)
		code16 |= ( ( is8Bit( elem1 ) || is8Bit( elem2 ) ? MEM_CLASS_8BIT : MEM_CLASS_16BIT ) << 1 );
		
		// set the register code
		// code: mmrr rmmm iiii ivsc (16/16 bits)
		code16 |= ( elem2.isMemoryReference() ? SRC_CODE_SECONDARY : SRC_CODE_PRIMARY );
		
		// encode the instruction
		if( isConstant )
			encoder.setWord( code16 );
		else
			encoder.setByte( code16 );
		return true;
	}
	
}
