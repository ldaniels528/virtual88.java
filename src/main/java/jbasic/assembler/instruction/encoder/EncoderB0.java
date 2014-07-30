package jbasic.assembler.instruction.encoder;

import static jbasic.assembler.instruction.encoder.X86AssemblyUtil.is8Bit;
import ibmpc.exceptions.X86AssemblyException;
import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.instruction.element.X86DataElement;
import jbasic.assembler.instruction.element.values.X86NumericValue;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * <pre>
 * Instruction Processor for instruction codes B0 thru BF 
 * ------------------------------------------------------------------
 * instruction				code 		tt rrr mmm	iiii c rrr dddd
 * ------------------------------------------------------------------
 * mov al,nn				B0nn					1011 0 000 nn
 * mov cl,nn				B1nn					1011 0 001 nn
 * mov dl,nn				B2nn					1011 0 010 nn
 * mov bl,nn				B3nn					1011 0 011 nn
 * mov ah,nn				B4nn					1011 0 100 nn
 * mov ch,nn				B5nn					1011 0 101 nn
 * mov dh,nn				B6nn					1011 0 110 nn
 * mov bh,nn				B7nn					1011 0 111 nn
 * mov ax,nnnn				B8nnnn					1011 1 000 nnnn
 * mov cx,nnnn				B9nnnn					1011 1 001 nnnn
 * mov dx,nnnn				BAnnnn					1011 1 010 nnnn
 * mov bx,nnnn				BBnnnn					1011 1 011 nnnn
 * mov sp,nnnn				BCnnnn					1011 1 100 nnnn
 * mov bp,nnnn				BDnnnn					1011 1 101 nnnn
 * mov si,nnnn				BEnnnn					1011 1 110 nnnn
 * mov di,nnnn				BFnnnn					1011 1 111 nnnn
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class EncoderB0 implements InstructionEncoder {
	private static final String INSTRUCTION_NAME = "MOV";
	private static final int MOV_TYPE3 = 0x0B; // 1011

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// make sure it's a MOV instruction (w/2 parameters)
		if( !INSTRUCTION_NAME.equals( instruction.getName() ) || 
				instruction.getParameterCount() != 2 ) return false;			
		
		// get the data element parameters
		final X86DataElement[] elems = instruction.getParameters();
		
		// cache the data elements
		final X86DataElement elem1 = elems[0];
		final X86DataElement elem2 = elems[1];
		
		// if elem1 is not a register or elem2 is not a value, return
		if( !elem1.isRegister() || !elem2.isValue() ) return false;
		
		// instruction code layout
		// -----------------------------
		// 7654 3210 (8 bits)
		// iiii crrr 
		// -----------------------------
		// 	i -> [4 bit] instruction type  
		//	c -> [1 bit] memory class (8-bit=0, 16-bit=1)
		//	r -> [3 bit] register code (ax=000)		
		
		// set indicators
		final boolean is8bit = is8Bit( elem1 );
		final X86NumericValue value = (X86NumericValue)elem2;
		
		// build the 8-bit instruction
		int code8;
		
		// set the instruction portion to the code 
		// code: iiii .... (4/8 bits)
		code8 = ( MOV_TYPE3 << 4 );
		
		// set the register class
		// code: iiii c... (5/8 bits)
		code8 |= ( ( is8bit ? 0 : 1 ) << 3 );
		
		// set the register code
		// code: iiii crrr (8/8 bits)
		code8 |= ( elem1.getSequence() );
		
		// encode the instruction
		codebuf.setByte( code8 );
		if( is8bit ) 
			codebuf.setByte( value.getValue() );
		else
			codebuf.setWord( value.getValue() );
		return true;		
	}

}
