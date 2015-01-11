package jbasic.gwbasic.values;

import jbasic.common.tokenizer.TokenIterator;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.values.Value;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Represents a Composition of GWBASIC Values
 * <br>Example: Z * ( Y - 6 )
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicCompositeValue implements Value {
    private final Collection elements;

    /**
     * Creates an instance of an expression
     *
     * @param it the given {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    public GwBasicCompositeValue(TokenIterator it)
            throws JBasicException {
        this.elements = parseElements(it);
    }

    /**
     * Evaluates this expression
     *
     * @return the resultant {@link Value value} of the evaluation of this expression
     */
    public MemoryObject getValue(JBasicCompiledCodeReference program) {
        MemoryObject returnObject = null;

        // iterate thru the elements
        for (final Iterator i = elements.iterator(); i.hasNext(); ) {
            final Object element = i.next();

            // if the return object is null, then we're dealing with
            // the first element, captue it's value.
            if (returnObject == null) {
                final Value value = (Value) element;
                returnObject = value.getValue(program).duplicate();
            }

            // next must be an operator ...
            else {
                // next element is an operator
                final GwBasicMathOperator operator = (GwBasicMathOperator) element;

                // next element must be a value
                final Value value = (Value) i.next();
                final MemoryObject currentObject = value.getValue(program);

                // is the return object numeric?
                if (returnObject.isNumeric()) {
                    // if the current object is not numeric, error ...
                    if (!currentObject.isNumeric())
                        throw new TypeMismatchException(currentObject);

                    // compute the return value
                    final NumberMemoryObject returnNum = (NumberMemoryObject) returnObject;
                    final NumberMemoryObject currentNum = (NumberMemoryObject) currentObject;
                    returnObject = operator.operate(returnNum, currentNum);
                }

                // is the return object a string?
                else if (returnObject.isString()) {
                    // cast the return object as a string
                    final StringMemoryObject returnString = (StringMemoryObject) returnObject;
                    // operator on the objects
                    operator.operate(returnString, currentObject);
                }
            }
        }
        return returnObject;
    }

    /**
     * Parses the given token iterator into an evaluatable expression.
     *
     * @param it the given {@link TokenIterator token iterator}
     * @return a {@link Collection collection} of elements that compose
     * an evaluatable expression.
     * @throws JBasicException
     */
    @SuppressWarnings("unchecked")
    private Collection parseElements(TokenIterator it)
            throws JBasicException {
        // create a container for the elements
        final Collection elements = new LinkedList();

        // loop through all tokens
        while (it.hasNext()) {
            // add the value
            final Value value = GwBasicValues.getValue(it);
            elements.add(value);

            // if there's another token ...
            if (it.hasNext()) {
                // peek at the next token
                final String token = it.peekAtNext();

                // if the token is an operator, add it
                if (GwBasicMathOperator.isOperator(token)) {
                    elements.add(new GwBasicMathOperator(it.next()));
                }
                // otherwise return the elements we have
                else return elements;
            }
        }
        return elements;
    }

    /**
     * @return a string representation of the value.
     */
    public String toString() {
        return elements.toString();
    }

}