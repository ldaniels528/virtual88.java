package org.ldaniels528.javapc.ibmpc.compiler;


import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;

import java.util.List;

/**
 * Represents an 80x86 instruction
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Instruction {
    private static final X86DataElement[] NO_PARAMS = new X86DataElement[0];
    private final X86DataElement[] params;
    private final String name;

    /**
     * Creates a new instruction
     *
     * @param name   the instruction's name
     * @param params the array of instruction {@link X86DataElement parameters}
     */
    protected X86Instruction(final String name, final X86DataElement[] params) {
        this.name = name;
        this.params = params;
    }

    /**
     * Creates a new instruction
     *
     * @param instructionName the instruction's name
     */
    public static X86Instruction create(final String instructionName) {
        return new X86Instruction(instructionName, NO_PARAMS);
    }

    /**
     * Creates a new instruction
     *
     * @param instructionName the instruction's name
     * @param params          the array of instruction {@link X86DataElement parameters}
     */
    public static X86Instruction create(final String instructionName, final X86DataElement... params) {
        return new X86Instruction(instructionName, params);
    }

    /**
     * Creates a new instruction
     *
     * @param instructionName the instruction's name
     * @param params          the {@link List list} of instruction {@link X86DataElement parameters}
     */
    public static X86Instruction create(final String instructionName, final List<X86DataElement> params) {
        // convert the list of parameters into an array
        final X86DataElement[] elements = (X86DataElement[]) params.toArray(new X86DataElement[params.size()]);

        // create the new instruction
        return new X86Instruction(instructionName, elements);
    }

    /**
     * Returns the instruction's name
     *
     * @return the instruction's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the instruction parameters
     *
     * @return the array of instruction {@link X86DataElement parameters}
     */
    public X86DataElement[] getParameters() {
        return params;
    }

    /**
     * Returns the number of parameters
     *
     * @return the number of parameters
     */
    public int getParameterCount() {
        return params.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32);
        sb.append(name).append(' ');
        int n = 0;
        for (final X86DataElement param : params) {
            if (n++ > 0) {
                sb.append(',');
            }
            sb.append(param);
        }
        return sb.toString();
    }

}
