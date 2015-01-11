package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Syntax: LOCK
 *
 * Locks the BUS
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class LOCK extends AbstractOpCode {
    private static LOCK instance = new LOCK();

    /**
     * Private constructor
     */
    private LOCK() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static LOCK getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        // TODO determine how to lock the bus
    }

}
