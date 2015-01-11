package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * No Operation (NOP)
 *
 * @author lawrence.daniels@gmail.com
 */
public class NOP extends AbstractOpCode {
    private static NOP instance = new NOP();

    /**
     * Private constructor
     */
    private NOP() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static NOP getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        // do nothing
    }

}

