package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

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
	 * @see org.ldaniels528.javapc.ibmpc.program.OpCode#execute(org.ldaniels528.javapc.ibmpc.program.IbmPcProgram, org.ldaniels528.javapc.ibmpc.IbmPcEnvironment)
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
