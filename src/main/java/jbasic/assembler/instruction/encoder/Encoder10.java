/**
 * 
 */
package jbasic.assembler.instruction.encoder;

import ibmpc.exceptions.X86AssemblyException;
import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes between 10h and 1Fh
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
 * adc	[bx],al				0710		00 000 111 	000100 0 0
 * adc	[bx],ax				0711		00 000 111 	000100 0 1
 * adc	al,[bx]				0712		00 000 111 	000100 1 0
 * adc	ax,[bx]				0713		00 000 111 	000100 1 1
 * adc	al,nn				  14					000101 0 0 nn
 * adc	ax,nnnn				  15					000101 0 1 nnnnn
 * push ss					  16					000101 1 0
 * pop ss					  17					000101 1 1
 * sbb	[bx],al				0718		00 000 111 	000110 0 0
 * sbb	[bx],ax				0719		00 000 111 	000110 0 1 
 * sbb	al,[bx]				071A		00 000 111 	000110 1 0 
 * sbb	ax,[bx]				071B		00 000 111 	000110 1 1 
 * sbb	al,nn			  	  1C					000111 0 0 nn
 * sbb	ax,nnnn				  1D					000111 0 1 nnnnn
 * push ds					  1E					000111 1 0
 * pop ds					  1F					000111 1 1
 * </pre> 
 * @author lawrence.daniels@gmail.com
 */
public class Encoder10 implements InstructionEncoder {

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		return false;
	}

}
