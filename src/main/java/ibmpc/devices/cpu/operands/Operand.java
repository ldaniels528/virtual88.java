package ibmpc.devices.cpu.operands;

import ibmpc.devices.cpu.OpCode;

/**
 * Represent an 80x86 operand to be evaluated within the execution of an {@link OpCode opCode}
 *
 * @author lawrence.daniels@gmail.com
 */
public interface Operand {
    int SIZE_8BIT = 8;
    int SIZE_16BIT = 16;
    int SIZE_32BIT = 32; // TODO limit to 20-bits?

    /**
     * Returns the value contained within this element
     *
     * @return the value contained within this element
     */
    int get();

    /**
     * Sets the value of this element
     *
     * @param value the given value
     */
    void set(int value);

    /**
     * Returns the enumeration indicating the
     * size of the operand in bits (e.g. 1 byte = 8 bits)
     *
     * @return the enumeration indicating the
     * size of the operand in bits
     */
    int size();

}
