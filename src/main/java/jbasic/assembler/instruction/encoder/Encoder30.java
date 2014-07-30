package jbasic.assembler.instruction.encoder;

import ibmpc.exceptions.X86AssemblyException;
import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes between 30h and 3Fh
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
 * ------------------------------------------------------------------
 * xor [bx],al				0730		00 000 111	001100 0 0
 * xor [bx],ax				0731		00 000 111 	001100 0 1
 * xor al,[bx]				0732		00 000 111	001100 1 0
 * xor ax,[bx]				0733		00 000 111 	001100 1 1
 * xor al,nn				  34					001101 0 0 nn
 * xor ax,nnnn				  35					001101 0 1 nnnn
 * ss:						  36					001101 1 0
 * aaa						  37					001101 1 1
 * cmp [bx],al				  38					001110 0 0
 * cmp [bx],ax				0739		00 000 111 	001110 0 1
 * cmp al,[bx]				073A		00 000 111	001110 1 0
 * cmp ax,[bx]				073B		00 000 111 	001110 1 1
 * cmp al,nn				  3C					001111 0 0 nn
 * cmp ax,nnnn				  3D					001111 0 1 nnnnn
 * ds:						  3E					001111 1 0
 * aas						  3F					001111 1 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder30 implements InstructionEncoder {

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		return false;
	}

}
