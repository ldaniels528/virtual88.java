package org.ldaniels528.javapc.ibmpc.devices.cpu.registers;

/**
 * Represents a 16-bit register implementation
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Register16bit extends X86GenericRegister {

    /**
     * Constructs a new 16-bit register
     *
     * @param name  the name of this register
     * @param index the given index of this register
     */
    public X86Register16bit(String name, int index) {
        super(name, 0xFFFF, index);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return SIZE_16BIT;
    }

}
