/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler.encoder;

import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.compiler.InstructionEncoder;
import org.ldaniels528.javapc.ibmpc.compiler.X86Instruction;
import org.ldaniels528.javapc.ibmpc.util.X86CodeBuffer;

/**
 * Processes instruction codes between 60h and 6Fh
 * @author lawrence.daniels@gmail.com
 */
public class Encoder60 implements InstructionEncoder {

	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.x86.instruction.InstructionEncoder#assemble(org.ldaniels528.javapc.ibmpc.x86.util.X86CodeBuffer, org.ldaniels528.javapc.ibmpc.x86.instruction.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer codebuf, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// TODO Auto-generated method stub
		return false;
	}

}
