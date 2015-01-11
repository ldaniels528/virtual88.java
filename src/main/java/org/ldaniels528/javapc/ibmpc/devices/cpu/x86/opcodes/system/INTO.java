package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Interrupt on Overflow
 *
 * Usage:  INTO
 * Modifies flags: IF TF
 *
 * If the Overflow Flag is set this instruction generates an INT 4
 * which causes the code addressed by 0000:0010 to be executed.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class INTO extends AbstractOpCode {
    private static INTO instance = new INTO();

    /**
     * Private constructor
     */
    private INTO() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static INTO getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        // if the overflow flag is set, call interrupt 4
        if (cpu.FLAGS.isOF()) {
            new INT(0x04).execute(system, cpu);
        }
    }

}
