package org.ldaniels528.javapc.ibmpc.app;

import org.ldaniels528.javapc.ibmpc.compiler.X86Instruction;
import org.ldaniels528.javapc.ibmpc.compiler.X86InstructionParser;
import org.ldaniels528.javapc.ibmpc.compiler.encoder.X86Assembler;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.util.X86CodeBuffer;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.tokenizer.Tokenizer;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenizerContext;

import static org.ldaniels528.javapc.ibmpc.util.X86CompilerUtil.getConfiguredTokenizer;

/**
 * Represents an IBM PC Assembly Language Compiler
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcAssemblyLanguageCompiler {
    private static final Tokenizer tokenizer = getConfiguredTokenizer();

    /**
     * Encodes the given assembly language statement into x86 machine code
     *
     * @param lines the given assembly language statement(s)
     * @return the resultant 8086 machine code
     * @throws X86AssemblyException
     */
    public static byte[] encode(final String... lines) throws X86AssemblyException {
        // create an encoding context for this session
        final X86CodeBuffer codeBuffer = new X86CodeBuffer(0xFFFF);

        // create an 8086 encoder instance
        final X86Assembler encoder = new X86Assembler(codeBuffer);

        // encode each instruction
        for (final String line : lines) {
            // tokenize the line
            final TokenizerContext context = tokenizer.parse(line);
            final TokenIterator it = tokenizer.nextTokens(context);

            // parse the assembly code into an instruction code
            final X86Instruction instruction = X86InstructionParser.parse(it);

            // encode the instruction
            encoder.encode(instruction);
        }

        // return the byte code
        return codeBuffer.toByteArray();
    }

}