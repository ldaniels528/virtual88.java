package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.console;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.common.values.impl.SimpleVariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * LINE INPUT Instruction
 * Syntax: LINE INPUT <var$>
 * Example: LINE INPUT NAME$
 */
public class LineInputOp extends GwBasicCommand {
    private VariableReference reference;
    private Value output;

    /**
     * Creates an instance of this opCode
     *
     * @param it the parsed text that describes the BASIC instruction
     * @throws JBasicException
     */
    public LineInputOp(TokenIterator it) throws JBasicException {
        parse(it);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(JBasicCompiledCode program) throws JBasicException {
        try {
            // get the screen instance
            final IbmPcDisplay screen = program.getSystem().getDisplay();

            // if output text was specified, display it
            if (output != null) {
                screen.write(output.getValue(program).toString());
                screen.update();
            }

            // lookup the variable
            final Variable variable = reference.getVariable(program);

            // get a scanner to read input
            final IbmPcKeyboard scanner = program.getSystem().getKeyboard();

            // get a line of input
            final String input = scanner.nextLine();

            // put the content into the variable
            final MemoryObject varContent = variable.getValue(program);
            varContent.setContent(input);
        } catch (InterruptedException e) {
            throw new JBasicException(e);
        }
    }

    /**
     * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
     * that will be displayed at runtime
     *
     * @param it the given {@link java.util.Iterator iterator}
     * @throws org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException
     */
    private void parse(TokenIterator it) throws JBasicException {
        if (!it.hasNext()) throw new JBasicException("Keyword 'INPUT' expected");
        it.next();
        try {
            while (it.hasNext()) {
                // look for output text
                if (GwBasicValues.isStringConstant(it.peekAtNext())) {
                    output = GwBasicValues.getValue(it);
                }

                // must be a variable or numeric value
                else {
                    reference = new SimpleVariableReference(it.next());
                }

                // there there are more elements; check for a command separator (semicolon)
                if (it.hasNext()) {
                    if (!it.peekAtNext().equals(";") && !it.peekAtNext().equals(","))
                        throw new JBasicException(it.next());
                    else it.next();
                }
            }
        } catch (TypeMismatchException e) {
            throw new JBasicException(e);
        }
    }

}
