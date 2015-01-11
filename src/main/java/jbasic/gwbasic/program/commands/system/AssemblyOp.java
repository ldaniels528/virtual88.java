package jbasic.gwbasic.program.commands.system;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * Assembly Language Command
 * <br>Syntax: !<i>assembly code</i>
 * <br>Example: !INT &H21
 * @author lawrence.daniels@gmail.com
 */
public class AssemblyOp extends GwBasicCommand {
	private final String asmCode;
	private int offset;

	/**
	 * Creates an instance of this opCode
	 * @param it the given {@link TokenIterator token iterator}
	 */
	public AssemblyOp( TokenIterator it ) 
	throws JBasicException {
		this.asmCode = it.toString();
		this.offset	= -1;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.program.OpCode#execute(ibmpc.program.IbmPcProgram, ibmpc.IbmPcEnvironment)
	 */
	public void execute( final JBasicCompiledCode program ) 
	throws JBasicException {
		if( offset == -1 ) {
			// compile the assembly language statements
			offset = program.assemble( asmCode, "INT &H20" );
		}
		
		// execute the assembly code
		program.executeAssembly( offset );		
	}	
		
}
