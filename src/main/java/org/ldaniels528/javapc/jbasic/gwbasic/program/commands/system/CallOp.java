package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.ProgramArguments;
import org.ldaniels528.javapc.ibmpc.devices.cpu.ProgramContext;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import java.util.LinkedList;
import java.util.List;

/**
 * CALL Command
 * <br>Syntax: CALL <i>numvar</i>[<i>(variables)</i>]
 * <br>Purpose: To call an assembly (or machine) language subroutine.
 * <pre>
 * 100 DEF SEG=&H2000
 * 110 ACC=&H7FA
 * 120 CALL ACC(A, B$, C)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com lawrence.daniels@gmail.com
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.DefSegOp
 */
public class CallOp extends GwBasicCommand {
    private final VariableReference variable;
    private final Value[] arguments;

    /**
     * Default constructor
     *
     * @param it the given {@link TokenIterator token iterator}
     */
    public CallOp(TokenIterator it) throws JBasicException {
        // get the numeric variable reference
        this.variable = GwBasicValues.getVariableReference(it);

        // expect "("
        JBasicTokenUtil.mandateToken(it, "(");

        // get the arguments
        this.arguments = parseArguments(it);

        // expect ")"
        JBasicTokenUtil.mandateToken(it, ")");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final JBasicCompiledCode compiledCode) throws JBasicException {
        // get the execution environment
        final GwBasicEnvironment environment = compiledCode.getEnvironment();

        // get the variable's value object
        // NOTE: The numeric value is a pointer, get the actual offset from it
        final MemoryObject object = variable.getValue(compiledCode);
        if (!object.isNumeric())
            throw new TypeMismatchException(object);

        // get the offset of the code to execute
        final int offset = (int) object.toDoublePrecision();

        // get the CPU instance
        final Intel80x86 cpu = compiledCode.getSystem().getCPU();

        // create the addressable arguments
        final AddressableArgument[] addressables = getAddressableArguments(compiledCode, arguments);

        // create an execution context
        final ProgramContext context =
                new ProgramContext(
                        compiledCode.getCodeSegment(),
                        compiledCode.getDataSegment(),
                        offset,
                        addressables
                );

        // execute the code
        try {
            cpu.execute(environment, context);
        } catch (final X86AssemblyException e) {
            throw new JBasicException(e);
        }
    }

    /**
     * Parses the arguments of the CALL
     *
     * @param it the given {@link TokenIterator token iterator}
     * @return the arguments of the CALL
     * @throws JBasicException
     */
    private Value[] parseArguments(TokenIterator it) throws JBasicException {
        // create a container for returning the values
        final List<Value> values = new LinkedList<Value>();

        // if there's at least one argument ...
        while (it.hasNext()) {
            // get the value
            final Value value = GwBasicValues.getValue(it);

            // add the value to our list
            values.add(value);

            // if there's another argument, it must be ","
            if (it.hasNext()) {
                JBasicTokenUtil.mandateToken(it, ",");

                // there must be another identifer
                if (!it.hasNext())
                    throw new SyntaxErrorException();
            }
        }

        // return the values
        return values.toArray(new Value[values.size()]);
    }

    private AddressableArgument[] getAddressableArguments(final JBasicCompiledCode program,
                                                          final Value[] values) {
        final AddressableArgument[] arguments = new AddressableArgument[values.length];
        for (int n = 0; n < values.length; n++) {
            arguments[n] = new AddressableArgument(program, values[n]);
        }
        return arguments;
    }

    /**
     * Represents an addressable argument
     *
     * @author lawrence.daniels@gmail.com
     */
    private class AddressableArgument implements ProgramArguments {
        private final JBasicCompiledCode program;
        private final Value value;

        public AddressableArgument(final JBasicCompiledCode program, final Value value) {
            this.program = program;
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getOffset() {
            final MemoryObject memoryObject = value.getValue(program);
            return memoryObject.getOffset();
        }

    }

}
