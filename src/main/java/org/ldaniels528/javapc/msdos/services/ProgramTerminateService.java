/**
 *
 */
package org.ldaniels528.javapc.msdos.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Program Terminate Service
 *
 * @author ldaniels
 */
public class ProgramTerminateService implements InterruptHandler {
    private static final ProgramTerminateService instance = new ProgramTerminateService();

    /**
     * Default constructor
     */
    private ProgramTerminateService() {
        super();
    }

    /**
     * @return the instance
     */
    public static ProgramTerminateService getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        cpu.halt();
    }

}
