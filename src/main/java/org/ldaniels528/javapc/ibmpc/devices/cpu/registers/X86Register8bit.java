package org.ldaniels528.javapc.ibmpc.devices.cpu.registers;

/**
 * Represents an 8-bit register implementation
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Register8bit extends X86GenericRegister {

    /**
     * Constructs a new 8-bit register
     *
     * @param name  the name of this register
     * @param index the given index of this register
     */
    public X86Register8bit(String name, int index) {
        super(name, 0xFF, index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return SIZE_8BIT;
    }

}
