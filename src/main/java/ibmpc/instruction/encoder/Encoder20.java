/**
 * 
 */
package ibmpc.instruction.encoder;

import ibmpc.exceptions.X86AssemblyException;
import ibmpc.instruction.InstructionEncoder;
import ibmpc.instruction.X86Instruction;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Processes instruction codes between 20h and 2Fh
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
 * and	[bx],al				0720		00 000 111 	001000 0 0
 * and	[bx],ax				0721		00 000 111 	001000 0 1
 * and	al,[bx]				0722		00 000 111 	001000 1 0
 * and	ax,[bx]				0723		00 000 111 	001000 1 1
 * and	al,nn				  24					001001 0 0 nn
 * and	ax,nnnn				  25					001001 0 1 nnnn 
 * es:						  26					001001 1 0
 * daa						  27					001001 1 1
 * sub	[bx],al				0728		00 000 111 	001010 0 0
 * sub	[bx],ax				0729		00 000 111 	001010 0 1
 * sub	al,[bx]				072A		00 000 111 	001010 1 0
 * sub	ax,[bx]				072B		00 000 111 	001010 1 1
 * sub	al,nn				  2C					001011 0 0 nn
 * sub	ax,nnnn				  2D					001011 0 1 nnnnn
 * cs:						  2E					0010 111 0
 * das						  2F					0010 111 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Encoder20 implements InstructionEncoder {

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		return false;
	}

}
