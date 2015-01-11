package org.ldaniels528.javapc.jbasic.gwbasic.values;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.NumberMemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.StringMemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalOperatorException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Represents a GWBASIC Math Operator
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicMathOperator {
    public static final int MUL = 0;
    public static final int DIV = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int POW = 4;
    public static final int MOD = 5;
    private static final List<String> OPERATORS = asList("*", "/", "+", "-", "^", "MOD");
    private int operatorId;

    /**
     * Creates an instance of this operator
     *
     * @param operator the given operator
     * @throws JBasicException
     */
    public GwBasicMathOperator(String operator)
            throws JBasicException {
        this.operatorId = getOperatorType(operator);
    }

    /**
     * Computes the result of the the given numbers using the this operator
     *
     * @param numberA the left side number
     * @param numberB the right side number
     * @return the result of the two numbers operated upon by the operator
     */
    public MemoryObject operate(NumberMemoryObject numberA, NumberMemoryObject numberB) {
        switch (operatorId) {
            case MUL:
                return numberA.multiply(numberB);
            case DIV:
                return numberA.divide(numberB);
            case ADD:
                return numberA.add(numberB);
            case SUB:
                return numberA.subtract(numberB);
            case POW:
                return numberA.exponent(numberB);
            case MOD:
                return numberA.modulus(numberB);
            default:
                throw new IllegalOperatorException();
        }
    }

    /**
     * Computes the result of the the given string and object using the this operator
     *
     * @param string the given {@link StringMemoryObject string}
     * @param object the given {@link MemoryObject object}
     */
    public void operate(StringMemoryObject string, MemoryObject object) {
        // if the operator is not an add, error ...
        if (operatorId != ADD) {
            throw new IllegalOperatorException();
        }

        // append the object   
        string.append(object);
    }

    /**
     * Returns the ID of the operator
     *
     * @return the ID of the operator
     */
    public int getID() {
        return operatorId;
    }

    /**
     * Returns the type of the given operator
     *
     * @param operator the given operator (*, /, +, -)
     * @return the type of the operator
     */
    private int getOperatorType(String operator) {
        return OPERATORS.indexOf(operator);
    }

    /**
     * Indicates whether the given symbol is a valid GWBASIC Operator
     *
     * @param symbol the given symbol
     * @return true, if the given symbol is a valid GWBASIC Operator
     */
    public static boolean isOperator(String symbol) {
        return OPERATORS.contains(symbol);
    }

}