package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

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
     * @param cpu    the given {@link Intel80x86 Intel 80x86 CPU} instance
     * @throws X86AssemblyException
     */
    void process(IbmPcSystem system, Intel80x86 cpu) throws X86AssemblyException;

}
