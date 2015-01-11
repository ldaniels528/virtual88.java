/**
 *
 */
package org.ldaniels528.javapc.jbasic.gwbasic.functions;

import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunction;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicTempString;

/**
 * INPUT$(n) Function
 * <br>Syntax: INPUT$(<i>n</i>)
 * <br>Example: str$ = INPUT$(1)
 *
 * @author lawrence.daniels@gmail.com
 */
public class InputFunction extends JBasicFunction {

    /**
     * Creates an instance of this function
     *
     * @param name the name of this function
     * @param it   the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    protected InputFunction(String name, TokenIterator it) throws JBasicException {
        super(name, it, TYPE_NUMERIC);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemoryObject getValue(JBasicCompiledCodeReference program) {
        try {
            // get the parameter values
            final MemoryObject[] objects = getParameterValues(program);

            // get the number of characters
            final int nchars = objects[0].toInteger();

            // get the keyboard instance
            final IbmPcKeyboard keyboard = program.getSystem().getKeyboard();

            // read the given number of characters from the keyboard
            final String data = keyboard.next(nchars);

            // return the value
            return new JBasicTempString(data);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
