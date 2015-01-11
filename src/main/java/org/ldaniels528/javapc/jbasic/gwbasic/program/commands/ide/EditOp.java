package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide;

import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicProgramStatement;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.GwBasicProgram;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import java.util.SortedSet;

/**
 * EDIT Command
 * <br>Syntax: EDIT <i>linenumber</i>
 * <br>Example 1: EDIT 100
 * <br>Example 2: EDIT .
 */
public class EditOp extends GwBasicCommand {
    private Value parameter;

    /**
     * Creates an instance of this opCode
     *
     * @param it the parsed text that describes the BASIC instruction
     * @throws JBasicException
     */
    public EditOp(final TokenIterator it) throws JBasicException {
        parse(it);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final JBasicCompiledCode compiledCode) throws JBasicException {
        try {
            // get the GWBASIC environment
            final GwBasicEnvironment environment = (GwBasicEnvironment) compiledCode.getSystem();

            // get the environemnts keyboard input device
            final IbmPcKeyboard scanner = environment.getKeyboard();

            // get the memory resident program
            final GwBasicProgram program = environment.getProgram();

            // get the statment
            final JBasicProgramStatement statement = getStatement(compiledCode, program, parameter);

            // get the line from the program
            final String currentCode = statement.getCode();

            // get the modified code
            final String modifiedCode = scanner.nextLine(currentCode);

            // overwrite the old code
            statement.setCode(modifiedCode);
        } catch (InterruptedException e) {
            throw new JBasicException(e);
        }
    }

    /**
     * Parses the given token iterator
     *
     * @param it the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    private void parse(final TokenIterator it) throws JBasicException {
        // get the line number
        parameter = GwBasicValues.getValue(it);

        // no more tokens allowed
        JBasicTokenUtil.noMoreTokens(it);
    }

    /**
     * Returns the statement referenced by the giving parameter
     *
     * @param compiledCode the given {@link JBasicCompiledCode compiled code}
     * @param program      the given {@link GwBasicProgram GWBASIC program}
     * @param parameter    the given {@link Value value}
     * @return the {@link JBasicProgramStatement statement} referenced by the giving parameter
     * @throws JBasicException
     */
    private JBasicProgramStatement getStatement(final JBasicCompiledCode compiledCode,
                                                final GwBasicProgram program,
                                                final Value parameter) throws JBasicException {
        // get a reference to the program
        final SortedSet<JBasicProgramStatement> statements = program.getStatements();

        // get the line number
        final Integer lineNumber = getLineNumber(compiledCode, program, statements, parameter);

        // try to find the line
        for (JBasicProgramStatement statement : statements) {
            if (statement.getLineNumber().equals(lineNumber))
                return statement;
        }

        // if the statement wasn't found, error ..
        throw new JBasicException("line " + lineNumber + " not found");
    }

    /**
     * Get the referenced line number
     *
     * @param compiledCode the given {@link JBasicCompiledCode compiled code}
     * @param program      the given {@link GwBasicProgram GWBASIC program}
     * @param statements   the given {@link SortedSet set} of {@link JBasicProgramStatement statements}
     * @throws JBasicException
     */
    private Integer getLineNumber(final JBasicCompiledCode compiledCode,
                                  final GwBasicProgram program,
                                  final SortedSet<JBasicProgramStatement> statements,
                                  final Value parameter) throws JBasicException {
        // get the parameter's type
        final MemoryObject typedValue = parameter.getValue(compiledCode);

        // is the parameter a string (must be ".")
        if (typedValue.isString()) {
            final String s = typedValue.toString();
            if (!s.equals("."))
                throw new JBasicException("Illegal argument");
            else
                return statements.first().getLineNumber();
        }

        // is the parameter an integer
        else if (typedValue.isNumeric()) {
            // get the line number
            return parameter.getValue(compiledCode).toInteger();
        }

        // must be a type mismatch
        else throw new TypeMismatchException(typedValue);
    }

}
