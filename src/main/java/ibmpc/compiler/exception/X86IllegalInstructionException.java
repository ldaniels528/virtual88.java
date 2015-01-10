package ibmpc.compiler.exception;


import ibmpc.exceptions.X86AssemblyException;
import ibmpc.compiler.X86Instruction;

/**
 * Represents the error that occurs when the compiler encounters
 * or invalid or malformed 80x86 instruction.
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86IllegalInstructionException extends X86AssemblyException {

	/**
	 * Creates a new Illegal Instruction Exception
	 */
	public X86IllegalInstructionException() {
		super( "Illegal instruction" );
	}
	
	/**
	 * Creates a new Illegal Instruction Exception
	 * @param code the given instruction
	 */
	public X86IllegalInstructionException( final int code ) {
		super( String.format( "Illegal instruction (opCode %02X)", code ) );
	}

	/**
	 * Creates a new Illegal Instruction Exception
	 * @param code the given instruction
	 */
	public X86IllegalInstructionException( final String code ) {
		super( "Illegal instruction (opCode '" + code + "')"  );
	}
	
	/**
	 * Creates a new Illegal Instruction Exception
	 * @param instruction the given {@link X86Instruction instruction}
	 */
	public X86IllegalInstructionException( final X86Instruction instruction ) {
		super( "Illegal instruction (instruction '" + instruction + "')"  );
	}
	
}
