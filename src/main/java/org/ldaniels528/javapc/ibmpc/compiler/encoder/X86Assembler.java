package org.ldaniels528.javapc.ibmpc.compiler.encoder;

import org.ldaniels528.javapc.ibmpc.compiler.InstructionEncoder;
import org.ldaniels528.javapc.ibmpc.compiler.X86Instruction;
import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;
import org.ldaniels528.javapc.ibmpc.compiler.element.values.X86Value;
import org.ldaniels528.javapc.ibmpc.compiler.X86MalformedInstructionException;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.util.X86CodeBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 8086 Machine Code Assembler
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Assembler {
    private static final Collection<InstructionEncoder> encoders;
    private final X86CodeBuffer codeBuffer;

    static {
        encoders = new ArrayList<>(20);
        encoders.add(new EncoderDB());
        encoders.add(new Encoder00());
        encoders.add(new Encoder10());
        encoders.add(new Encoder20());
        encoders.add(new Encoder30());
        encoders.add(new Encoder40());
        encoders.add(new Encoder50());
        encoders.add(new Encoder60());
        encoders.add(new Encoder70());
        encoders.add(new Encoder80());
        encoders.add(new Encoder90());
        encoders.add(new EncoderA0());
        encoders.add(new EncoderB0());
        encoders.add(new EncoderC0());
        encoders.add(new EncoderD0());
        encoders.add(new EncoderE0());
        encoders.add(new EncoderF0());
    }

    /**
     * Default constructor
     */
    public X86Assembler(final X86CodeBuffer codeBuffer) {
        this.codeBuffer = codeBuffer;
    }

    /**
     * Encodes the given instruction storing it's binary result
     * in the given code buffer
     *
     * @param instruction the given {@link X86Instruction instruction}
     * @throws X86AssemblyException
     */
    public void encode(final X86Instruction instruction)
            throws X86AssemblyException {
        // encode the instruction
        for (final InstructionEncoder encoder : encoders) {
            if (encoder.assemble(codeBuffer, instruction)) {
                return;
            }
        }

        // if the instruction wasn't handled, error ...
        throw new X86MalformedInstructionException("Unrecognized instruction - " + instruction);
    }

    /**
     * "DB" Instruction Encoder (e.g. 'DB "hello world"')
     *
     * @author lawrence.daniels@gmail.com
     */
    private static class EncoderDB implements InstructionEncoder {
        private static final String INSTRUCTION_NAME = "DB";

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean assemble(final X86CodeBuffer codebuf, final X86Instruction instruction) throws X86AssemblyException {
            try {
                // make sure it's a DB instruction
                if (!INSTRUCTION_NAME.equals(instruction.getName())) return false;

                // encode the parameters
                final X86DataElement[] params = instruction.getParameters();
                for (final X86DataElement param : params) {
                    // if the parameter is not a value, error ...
                    if (!param.isValue()) {
                        throw new X86MalformedInstructionException("Value expected at '" + param + "'");
                    }

                    // cast it to a value
                    final X86Value value = (X86Value) param;

                    // encode the value
                    codebuf.setBytes(value.getBytes());
                }
                return true;
            } catch (IOException e) {
                throw new X86AssemblyException(e);
            }
        }

    }

}
