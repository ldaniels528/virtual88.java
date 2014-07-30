package jbasic.assembler;

import static jbasic.assembler.util.X86CompilerUtil.getConfiguredTokenizer;
import ibmpc.exceptions.X86AssemblyException;
import jbasic.assembler.instruction.X86Instruction;
import jbasic.assembler.instruction.X86InstructionParser;
import jbasic.assembler.instruction.encoder.X86Assembler;
import jbasic.assembler.util.X86CodeBuffer;

import com.ldaniels528.tokenizer.TokenIterator;
import com.ldaniels528.tokenizer.Tokenizer;
import com.ldaniels528.tokenizer.TokenizerContext;

/**
 * Represents a JBasic/IBM PC Assembly Language Compiler
 * @author lawrence.daniels@gmail.com
 */
public class JBasicAssemblyLanguageCompiler {	
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