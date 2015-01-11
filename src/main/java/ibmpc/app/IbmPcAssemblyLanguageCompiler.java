package ibmpc.app;

import static ibmpc.util.X86CompilerUtil.getConfiguredTokenizer;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.compiler.X86Instruction;
import ibmpc.compiler.X86InstructionParser;
import ibmpc.compiler.encoder.X86Assembler;
import ibmpc.util.X86CodeBuffer;

import jbasic.common.tokenizer.TokenIterator;
import jbasic.common.tokenizer.Tokenizer;
import jbasic.common.tokenizer.TokenizerContext;

/**
 * Represents an IBM PC Assembly Language Compiler
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcAssemblyLanguageCompiler {
	private static final Tokenizer tokenizer = getConfiguredTokenizer();
	
	/** 
	 * Encodes the given assembly language statement into x86 machine code
	 * @param lines the given assembly language statement(s)
	 * @return the resultant 80x86 machine code
	 * @throws X86AssemblyException 
	 */
	public static byte[] encode( final String ... lines ) 
	throws X86AssemblyException {
		// create an encoding context for this session
		final X86CodeBuffer codeBuffer = new X86CodeBuffer( 0xFFFF );
		
		// create an 80x86 encoder instance
		final X86Assembler encoder = new X86Assembler( codeBuffer );
		
		// encode each instruction
		for( final String line : lines ) {	
			// tokenize the line
			final TokenizerContext context = tokenizer.parse( line );
			final TokenIterator it = tokenizer.nextTokens( context );
			
			// parse the assembly code into an instruction code
			final X86Instruction instruction = X86InstructionParser.parse( it );
			
			// encode the instruction
			encoder.encode( instruction );
		}
		
		// return the byte code
		return codeBuffer.toByteArray();
	}
		
}