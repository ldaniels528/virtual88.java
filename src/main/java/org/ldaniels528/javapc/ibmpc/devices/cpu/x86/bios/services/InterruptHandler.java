package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Represents a generic interrupt handler
 *
 * @author ldaniels
 */
public interface InterruptHandler {

    /**
     * Process the Equipment Services Interrupt (INT 11h)
     *
     * @param system the given {@link IbmPcSystem IBM PC System} instance
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 Intel 80x86 CPU} instance
     * @throws X86AssemblyException
     */
    void process(IbmPcSystem system, Intel8086 cpu) throws X86AssemblyException;

}
