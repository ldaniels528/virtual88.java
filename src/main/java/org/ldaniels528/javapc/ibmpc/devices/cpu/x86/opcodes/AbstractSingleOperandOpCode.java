package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryPointer;

/**
 * Represents a Single Operand opCode (e.g. 'POP CS')
 *
 * @author ldaniels
 */
public abstract class AbstractSingleOperandOpCode extends AbstractOpCode {
    public final String name;
    public final Operand operand;

    /**
     * Creates a new dual operand opCode
     *
     * @param name the name of the instruction
     * @param operand  the given {@link Operand operand}
     */
    public AbstractSingleOperandOpCode(final String name, final Operand operand) {
        this.name = name;
        this.operand = operand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // if the operand is a memory pointer, point to it's memory reference
        final Object operandObj = (operand instanceof MemoryPointer)
                ? ((MemoryPointer) operand).getMemoryReference()
                : operand;

        // convert the instruction into a string
        return String.format("%s %s", name, operandObj);
    }

}
