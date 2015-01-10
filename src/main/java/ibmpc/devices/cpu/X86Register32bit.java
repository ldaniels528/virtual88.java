package ibmpc.devices.cpu;

import ibmpc.devices.cpu.x86.registers.X86GenericRegister;

/**
 * Represents a 32-bit register implementation
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Register32bit extends X86GenericRegister {

    /**
     * Constructs a new 32-bit register
     *
     * @param name  the name of this register
     * @param index the given index of this register
     */
    public X86Register32bit(String name, int index) {
        super(name, 0xFFFFFFFF, index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return SIZE_32BIT;
    }

}