package jbasic.assembler.instruction.encoder;

import static jbasic.assembler.instruction.element.registers.X86RegisterReferences.AL;
import static jbasic.assembler.instruction.element.registers.X86RegisterReferences.AX;
import static jbasic.assembler.instruction.encoder.X86AssemblyUtil.checkParameterCount;
import static jbasic.assembler.instruction.encoder.X86AssemblyUtil.is8Bit;
import static jbasic.assembler.instruction.encoder.X86AssemblyUtil.isGeneralPurposeRegister;
import ibmpc.exceptions.X86AssemblyException;
import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.instruction.element.X86DataElement;
import jbasic.assembler.instruction.exception.X86MalformedInstructionException;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes from 80h to 8Fh (e.g. "mov ax,[1234]")
 * ---------------------------------------------------------------------------
 * type		bits		description 				comments
 * ---------------------------------------------------------------------------
 * t		2			register/reference type		11=register	
 * r		3			register info				ax=000	
 * m		3			memory reference info	
 * i		5	 		instruction type  			mov=10001
 * v		1			constant value				0=non-constant, 1=constant
 * s		1			source (primary)			register=0, reference=1
 * c		1	 		memory class 	 			8-bit=0, 16-bit=1	
 * d		8/16		offset 
 *  
 *  Instruction code layout
 *  -----------------------------
 *  fedc ba98 7654 3210 (16 bits)
 *  ttrr rmmm iiii iisc  
 *  
 * ------------------------------------------------------------------
 * instruction				code 		tt jjj rrr	iiiiii s c dddd
 * ------------------------------------------------------------------	
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
 * instruction				code 		tt rrr mmm	iiiiii s c dddd
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
 * mov al,al				C088		11 000 000	100010 0 0 
 * mov cl,al				C188		11 000 001	100010 0 0 
 * mov al,cl				C888		11 001 000	100010 0 0 
 * mov [bx],ax				0789		00 000 111	100010 0 1 
 * mov ax,ax				C089		11 000 000	100010 0 1 
 * mov ax,cx				C889		11 001 000	100010 0 1 
 * mov al,[nnnn]			0E8A		00 001 110	100010 1 0
 * mov ax,[bx]				078B		00 000 111	100010 1 1 
 * 
 * mov ax,cs				C88C		11 001 000	100011 0 0 
 * lea ax,[bx]				078D		00 000 111 	100011 0 1
 * mov cs,ax				C88E		11 001 000	100011 1 0 
 * pop [bx]					078F		00 000 111	100011 1 1  
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder80 implements InstructionEncoder {
	private static final String INSTRUCTION_NAME = "MOV";
	
	private static final int MOV_TYPE1 = 0x08; // 1000
	private static final int MOV_TYPE2 = 0x0A; // 1010
	private static final int MOV_TYPE3 = 0x0B; // 1011
	private static final int MOV_TYPE4 = 0x0C; // 1100
	
	/* (non-Javadoc)
	 * @see ibmpc.app.compiler.instruction.encoder.InstructionEncoder#encode(ibmpc.app.compiler.instruction.X86CodeBuffer, ibmpc.app.compiler.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// make sure it's a MOV instruction
		if( !INSTRUCTION_NAME.equals( instruction.getName() ) ) return false;			
		
		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// check the parameter count
		checkParameterCount( elems, 2 );
		
		// cache the data elements
		final X86DataElement elem1 = elems[0];
		final X86DataElement elem2 = elems[1];
		
		// if elem1 a register ...
		if( elem1.isRegister() ) {
			// and elem2 is a register (MOV reg1,reg2) ...
			if( elem2.isRegister() ) {
				return assembleType1( codebuf, elem1, elem2 );
			}
			
			// and elem2 is a memory reference (MOV reg,mem) ...
			else if( elem2.isMemoryReference() ) {
				// if elem2 is AL or AX, it's Type 2
				// otherwise, it's Type 1
				return ( elem2.equals( AL ) || elem2.equals( AX ) )
					? null // assembleType2( codebuf, elem1, elem2 )
					: assembleType1( codebuf, elem1, elem2 );
			}
			
			// and elem2 is a numeric value (MOV reg,value) ...
			else if( elem2.isValue() ) {
				return assembleType3( codebuf, elem1, elem2 );
			}
		}
				
		// if elem1 is a memory reference (MOV mem,reg)...
		else if( elem1.isMemoryReference() ) {
			// and elem2 is register 
			if( elem2.isRegister()  ) {
				// if elem2 is AL or AX, it's Type 2
				// otherwise, it's Type 1
				return ( elem2.equals( AL ) || elem2.equals( AX ) )
					? null // assembleType2( codebuf, elem1, elem2 )
					: assembleType1( codebuf, elem1, elem2 );
			}
			
			// and elem2 is a numeric value (MOV word ptr [ref],nnnn) ...
			else if( elem2.isValue() ) {
				//return assembleType4( codebuf, elem1, elem2 );
			}
		}
		
		// for unhandled elements, error ...
		throw new X86MalformedInstructionException( "Unhandled arguments " + elem1 + " and " + elem2 ); 
	}
	
	/**
	 * Encodes a Type 1 MOV instructions (e.g. 'mov al,cl', 'mov [bx],al', 'mov ax,[bx]')
	 * @param codebuf the given {@link X86CodeBuffer code buffer}
	 * @param instruction the given {@link X86Instruction 80x86 instruction}
	 * @throws X86MalformedInstructionException 
	 */
	private boolean assembleType1( final X86CodeBuffer codebuf, final X86DataElement elem1, X86DataElement elem2 ) 
	throws X86MalformedInstructionException {
		// instruction code layout
		// -----------------------------
		// fedc ba98 7654 3210 (16 bits)
		// ttrr rmmm iiii ugsc 
		// -----------------------------
		//	t -> [2 bit] 	register/reference type		11=register	
		//	r -> [3 bit] 	register info				ax=000	
		//	m -> [3 bit] 	memory reference info	
		// 	i -> [4 bit] 	instruction type  		
		//	u -> [1 bit]	unknown						(always 1)
		//	g -> [1 bit] 	general purpose register	(non-constant=0, constant=1)
		//	s -> [1 bit] 	source type					(reference=0, register=1)
		//	c -> [1 bit] 	memory class	  			(8-bit=0, 16-bit=1)
		//	d -> [8/16 bit]	offset 
		
		// build the 16-bit instruction
		int code16;
		
		// set the register/reference element type (11b=register, 00b/01b/10b=reference)
		// code: tt.. .... .... .... (02/16 bits)
		code16 = ( ( elem1.isRegister() ? 0x02 : 0 ) << 14 );
		
		// set the instruction sub code bits 
		// code: ttrr r... .... .... (05/16 bits)
		code16 |= ( elem1.getSequence() << 11 );
		
		// set the target register index bits
		// code: ttrr rmmm .... .... (08/16 bits)
		code16 |= ( elem2.getSequence() << 8 );
		
		// set the instruction code bits
		// code: ttrr rmmm iiii .... (12/16 bits)
		code16 |= ( MOV_TYPE1 << 4 );
		
		// set the unknown bit (always 1)
		// code: ttrr rmmm iiii u... (13/16 bits)
		code16 |= ( 1 << 3 );
		
		// set the general purpose register bit 
		// code: ttrr rmmm iiii ug.. (14/16 bits)
		code16 |= ( ( isGeneralPurposeRegister( elem2 ) ? 0 : 1 ) << 2 );
		
		// set the source type
		// code: ttrr rmmm iiii ugs. (15/16 bits)
		code16 |= ( ( elem2.isRegister() ? 0 : 1 ) << 1 );
		
		// set the register class
		// code: ttrr rmmm iiii ugsc (16/16 bits)
		code16 |= ( is8Bit( elem1 ) ? 0 : 1 );
		
		// encode the instruction
		codebuf.setByte( code16 );
		return true;
	}
	
	/**
	 * Encodes a Type 3 MOV instructions (e.g. 'mov al,12', 'mov ax,1234')
	 * @param codebuf the given {@link X86CodeBuffer code buffer}
	 * @param instruction the given {@link X86Instruction 80x86 instruction}
	 * @throws X86MalformedInstructionException 
	 */
	private boolean assembleType3( final X86CodeBuffer codebuf, final X86DataElement elem1, X86DataElement elem2 ) 
	throws X86MalformedInstructionException {
		return false;
	}

}
