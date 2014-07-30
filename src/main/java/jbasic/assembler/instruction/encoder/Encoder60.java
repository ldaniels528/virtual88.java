/**
 * 
 */
package jbasic.assembler.instruction.encoder;

import ibmpc.exceptions.X86AssemblyException;
import jbasic.assembler.instruction.InstructionEncoder;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.util.X86CodeBuffer;

/**
 * Processes instruction codes between 60h and 6Fh
 * @author lawrence.daniels@gmail.com
 */
public class Encoder60 implements InstructionEncoder {

	/* (non-Javadoc)
	 * @see ibmpc.x86.instruction.InstructionEncoder#assemble(ibmpc.x86.util.X86CodeBuffer, ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		return false;
	}

}
